package com.hiricus.dcs.service;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.UserDataRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.util.documents.DocumentUtils;
import com.hiricus.dcs.util.table.GroupTableEntry;
import com.hiricus.dcs.util.table.TableCreator;
import com.hiricus.dcs.util.table.TableParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XmlTableService {
    private final TableParser tableParser;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final TableCreator tableCreator;
    private final GroupRepository groupRepository;

    public XmlTableService(TableParser tableParser,
                           UserService userService,
                           UserRepository userRepository,
                           UserDataRepository userDataRepository,
                           TableCreator tableCreator,
                           GroupRepository groupRepository) {
        this.tableParser = tableParser;
        this.userService = userService;
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
        this.tableCreator = tableCreator;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public void printGroupTable(DocumentObject document) {
        try {
            Workbook table = DocumentUtils.loadXlsx(document);
            List<List<String>> parsed = tableParser.parseTable(table, 9);
            List<GroupTableEntry> tableEntries = parsed.stream()
                    .map(GroupTableEntry::new)
                    .toList();

            for (GroupTableEntry tableEntry : tableEntries) {
                System.out.println(tableEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load table from document object");
        }
    }

    @Transactional
    public void handleGroupTable(DocumentObject document, Integer curatorId) {
        List<GroupTableEntry> entries;
        try {
            Workbook table = DocumentUtils.loadXlsx(document);
            List<List<String>> parsed = tableParser.parseTable(table, 9);
            entries = parsed.stream()
                    .map(GroupTableEntry::new)
                    .toList();

            System.out.println("Parsed GROUP_TABLE entries: ");
            for (GroupTableEntry tableEntry : entries) {
                System.out.println(tableEntry);
            }
            loadDataFromGroupTable(entries, curatorId);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load table from document object");
        }
    }

    @Transactional
    public DocumentObject getGroupInfoTable(Integer groupId) {
        Optional<GroupObject> group = groupRepository.findGroupById(groupId);
        if (group.isEmpty()) {
            throw new EntityNotFoundException("Group with id " + groupId + " not found");
        }
        String groupName = group.get().getName();

        List<UserDataObject> groupMembersData = groupRepository.getGroupMembersInfo(groupId);
        List<GroupTableEntry> entries = groupMembersData.stream()
                .map(userData -> new GroupTableEntry(userData, groupName))
                .toList();


        Workbook workbook = tableCreator.createGroupTable(entries);

        // TODO: тестовый кусок, убрать
//        try (FileOutputStream fos = new FileOutputStream("C:/Users/user/Desktop/Replacement_test/GROUP_TABLE_COLLECTED.xlsx")) {
//            workbook.write(fos);
//        } catch (Exception exception) {
//            System.out.println("Какая то фигня во время записи таблицы в файл");
//        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data;
        try {
            workbook.write(bos);
            data = bos.toByteArray();
            bos.close();
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Какая то фигня во время записи файла в байты");
        }

        return new DocumentObject(groupName + " info", ".xlsx", data);
    }

    private void loadDataFromGroupTable(List<GroupTableEntry> entries, Integer curatorId) {
        // Чтобы не лезть постоянно в базу для поиска группы
        Map<String, Integer> affectedGroups = new HashMap<>();

        for (GroupTableEntry entry : entries) {
            UserDataObject userData = new UserDataObject(entry);
            Integer userId;

            // Если id студента не передан - создаём нового студента
            if (entry.getId() == null) {
                userId = userService.createUserData(userData);
            } else {  // Если id передан - вносим изменения в данные уже существующего
                userService.changeUserData(userData);
                userId = entry.getId();
            }

            // Если группы нет в мапе
            if (!affectedGroups.containsKey(entry.getGroupName())) {
                // Если группы нет в системе - создаётся новая группа
                if (!groupRepository.isGroupExistsByName(entry.getGroupName())) {
                    Integer groupId = groupRepository.createEmptyGroupWithoutStaff(new GroupObject(entry.getGroupName())).get();
                    groupRepository.setCurator(groupId, curatorId);
                    affectedGroups.put(entry.getGroupName(), groupId);
                }

                // Если группы нет в мапе, но есть в системе - добавляем в мапу
                affectedGroups.put(entry.getGroupName(), groupRepository.findGroupByName(entry.getGroupName()).get().getId());
            }

            // Если пользователь уже в группе - удаляем его из группы
            if (userRepository.getUsersGroup(userId).isPresent()) {
                groupRepository.removeGroupMembersById(userRepository.getUsersGroup(userId).get().getId(), List.of(userId));
            }

            // Добавляем пользователя в новую группу
            Integer groupId = affectedGroups.get(entry.getGroupName());
            groupRepository.addGroupMembersById(groupId, List.of(userId));
        }
    }
}
