package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.repository.DisciplineRepository;
import com.hiricus.dcs.model.repository.GradeRepository;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.util.documents.DocumentUtils;
import com.hiricus.dcs.util.table.grade.GradeTableCreator;
import com.hiricus.dcs.util.table.grade.GradeTableEntry;
import com.hiricus.dcs.util.table.group.GroupTableEntry;
import com.hiricus.dcs.util.table.group.GroupTableCreator;
import com.hiricus.dcs.util.table.TableParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XmlTableService {
    private final TableParser tableParser;
    private final GroupTableCreator groupTableCreator;

    private final GradeTableCreator gradeTableCreator;

    private final UserService userService;
    private final UserRepository userRepository;

    private final GroupRepository groupRepository;
    private final DisciplineRepository disciplineRepository;

    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

    public XmlTableService(TableParser tableParser,
                           UserService userService,
                           UserRepository userRepository,
                           GroupTableCreator groupTableCreator,
                           GradeTableCreator gradeTableCreator,
                           GroupRepository groupRepository,
                           DisciplineRepository disciplineRepository,
                           GradeRepository gradeRepository,
                           GradeService gradeService) {
        this.tableParser = tableParser;
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupTableCreator = groupTableCreator;
        this.gradeTableCreator = gradeTableCreator;
        this.groupRepository = groupRepository;
        this.disciplineRepository = disciplineRepository;
        this.gradeRepository = gradeRepository;
        this.gradeService = gradeService;
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
            throw new RuntimeException("Cannot load table from group table");
        }
    }

    @Transactional
    public void handleGradeTable(DocumentObject document) {
        List<GradeTableEntry> entries;
        try {
            Workbook table = DocumentUtils.loadXlsx(document);
            List<List<String>> parsed = tableParser.parseTable(table, 7);
            entries = parsed.stream()
                    .map(GradeTableEntry::new)
                    .toList();

            System.out.println("Parsed GRADE_TABLE entries: ");
            for (GradeTableEntry entry : entries) {
                System.out.println(entry);
            }
            loadDataFromGradeTable(entries);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load data from grade table");
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


        Workbook workbook = groupTableCreator.createGroupTable(entries);

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

    @Transactional
    public DocumentObject getPersonalGradeReport(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        List<FinalGradeObject> personalGrades = gradeService.getUsersGrades(userId);
        Workbook workbook = gradeTableCreator.writePersonalGradeReport(personalGrades, userService.getUserFullName(userId));

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

        return new DocumentObject("Personal_grade_report", ".xlsx", data);
    }

    @Transactional
    public DocumentObject getGradeReportOnDiscipline(Integer disciplineId, Integer groupId) {
        if (!disciplineRepository.isExistsById(disciplineId)) {
            throw new EntityNotFoundException("Discipline not found");
        }
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group not found");
        }

        List<FinalGradeObject> grades = gradeService.getGradesByGroupAndDiscipline(groupId, disciplineId);
        String disciplineName = disciplineRepository.findDisciplineById(disciplineId).get().getName();
        String groupName = groupRepository.findGroupById(groupId).get().getName();

        Workbook workbook = gradeTableCreator.writeGradeReportOnDisciplineAndGroup(grades, disciplineName, groupName);

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

        return new DocumentObject("Grade_report_on_group_and_discipline", ".xlsx", data);
    }

    @Transactional
    public DocumentObject getFillingTemplateForGrades(Integer groupId, Integer disciplineId) {
        // Проверка на существование
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group not found");
        }
        if (!disciplineRepository.isExistsById(disciplineId)) {
            throw new EntityNotFoundException("Discipline not found");
        }

        // Собираем всех пользователей из группы и получаем название дисциплины
        List<UserDataObject> groupMembers = groupRepository.getGroupMembersInfo(groupId);
        String disciplineName = disciplineRepository.findDisciplineById(disciplineId).get().getName();

        // Формируем объекты для записи в таблицу
        List<GradeTableEntry> entries = groupMembers.stream()
                .map(userData -> new GradeTableEntry(userData, disciplineName))
                .toList();

        // Создаём объект таблицы
        Workbook workbook = gradeTableCreator.writeGradeTable(entries);

//        // TODO: тестовый кусок, убрать
//        try (FileOutputStream fos = new FileOutputStream("C:/Users/user/Desktop/Replacement_test/GRADE_TABLE_COLLECTED.xlsx")) {
//            workbook.write(fos);
//        } catch (Exception exception) {
//            System.out.println("Какая то фигня во время записи таблицы в файл");
//        }

        // И записываем его в документ
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data;
        try {
            workbook.write(bos);
            data = bos.toByteArray();
            workbook.close();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException("Какая то фигня во время записи файла в байты");
        }

        return new DocumentObject(disciplineName + " info", ".xlsx", data);
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

    // Поддерживает аплоад только для одной дисциплины за раз
    private void loadDataFromGradeTable(List<GradeTableEntry> entries) {
        Optional<DisciplineObject> disciplineOptional = disciplineRepository.findDisciplineByName(entries.getFirst().getDisciplineName());
        if (disciplineOptional.isEmpty()) {
            throw new EntityNotFoundException("Discipline '" + entries.getFirst().getDisciplineName() + "' does not exist");
        }

        Integer disciplineId = disciplineOptional.get().getId();
        for (GradeTableEntry entry : entries) {
            // Проверка существования пользователя
            if (entry.getUserId() == null || !userRepository.isUserExistsById(entry.getUserId())) {
                throw new EntityNotFoundException("Student not found");
            }

            // Добавление оценки
            FinalGradeObject grade = new FinalGradeObject(entry.getGrade(), LocalDate.now(), entry.getUserId(), disciplineId);
            gradeRepository.createGrade(grade);
        }
    }
}
