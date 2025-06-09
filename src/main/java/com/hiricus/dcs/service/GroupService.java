package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityAlreadyExistsException;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.exception.RoleViolationException;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.RoleRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.util.RoleContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final RoleRepository roleRepository;
    private final RoleContainer roleContainer;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupService(RoleRepository roleRepository,
                        RoleContainer roleContainer,
                        GroupRepository groupRepository,
                        UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleContainer = roleContainer;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<GroupObject> getAllGroups(boolean includeUsers) {
        List<Integer> groupIds = groupRepository.findAllGroupIds();
        List<GroupObject> groupObjects = new ArrayList<>();

        for (Integer groupId : groupIds) {
            groupObjects.add(constructFullGroupObject(groupId, includeUsers));
        }

        return groupObjects;
    }

    @Transactional
    public List<GroupObject> getCuratedGroups(int curatorId, boolean includeUsers) {
        List<Integer> groupIds = groupRepository.findCuratedGroupIds(curatorId);
        List<GroupObject> groupObjects = new ArrayList<>();

        for (Integer groupId : groupIds) {
            groupObjects.add(constructFullGroupObject(groupId, includeUsers));
        }

        return groupObjects;
    }

    @Transactional
    public GroupObject getGroupById(int groupId, boolean includeUsers) {
        return constructFullGroupObject(groupId, includeUsers);
    }

    @Transactional
    public Integer createGroup(GroupObject groupObject) {
        if (groupRepository.isGroupExistsByName(groupObject.getName())) {
            throw new EntityAlreadyExistsException("Group with name " + groupObject.getName() + " already exists");
        }

        // Проверяем есть ли у старосты и куратора нужные роли
        Boolean isCuratorValid = roleRepository.userHasRole(groupObject.getCurator().getId(), "ROLE_CURATOR");
        Boolean isHeadValid = roleRepository.userHasRole(groupObject.getHead().getId(), "ROLE_HEAD");
        if (!(isCuratorValid && isHeadValid)) {
            throw new RoleViolationException("Curator or head don't have enough privileges");
        }

        return groupRepository.createEmptyGroupWithStaff(groupObject).get();
    }

    @Transactional
    public void updateGroupExcludingStaff(GroupObject groupObject) {
        if (!groupRepository.isGroupExistsById(groupObject.getId())) {
            throw new EntityNotFoundException("Group does not exist");
        }

        groupRepository.updateGroupExcludingStaff(groupObject);
    }

    @Transactional
    public void deleteGroup(Integer groupId) {
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group does not exist");
        }

        // У старосты удаляется роль
        Optional<UserObject> groupHead = groupRepository.findHead(groupId);
        if (groupHead.isPresent()) {
            roleRepository.removeAllRolesFromUser(groupHead.get().getId());
            roleRepository.addRoleToUser(groupHead.get().getId(), roleContainer.getRoleId("ROLE_USER"));
        }

        groupRepository.deleteGroupById(groupId);
    }

    @Transactional
    public void addUsersToGroup(Integer groupId, List<Integer> userIds) {
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group does not exist");
        }

        // Проверка что все пользователи могут быть добавлены
        for (Integer userId : userIds) {
            if (!userCanBeAdded(groupId, userId)) {
                throw new EntityNotFoundException("User with id " + userId + " can't be added");
            }
        }

        List<UserObject> userObjects = userIds
                .stream()
                .map(UserObject::new)
                .toList();

        groupRepository.addGroupMembers(groupId, userObjects);
    }

    @Transactional
    public void removeUsersFromGroup(Integer groupId, List<Integer> userIds) {
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group does not exist");
        }

        // Проверка на то что все пользователи могут быть удалены
        for (Integer userId : userIds) {
            if (!userCanBeRemoved(groupId, userId)) {
                throw new EntityNotFoundException("User with id " + userId + " can't be removed");
            }
        }

        groupRepository.removeGroupMembers(groupId, userIds);
    }

    // ЗАПУСКАТЬ В ТРАНЗАКЦИИ
    private GroupObject constructFullGroupObject(Integer groupId, boolean includeUsers) {
        GroupObject groupObject = groupRepository.findGroupById(groupId).orElse(null);
        UserObject head = groupRepository.findHead(groupId).orElse(null);
        UserObject curator = groupRepository.findCurator(groupId).orElse(null);

        if (groupObject == null) {
            throw new EntityNotFoundException("Group with id " + groupId + " not found");
        }

        groupObject.setHead(head);
        groupObject.setCurator(curator);

        // В случе если нужно добавить всех членов группы
        if (includeUsers) {
            List<UserDataObject> members = groupRepository.getGroupMembersInfo(groupId);
            groupObject.setMembers(members);
        }

        return groupObject;
    }

    private boolean userCanBeAdded(int groupId, int userId) {
        boolean userExists = userRepository.isUserExistsById(userId);
        boolean userAlreadyInGroup = userRepository.getUsersGroup(userId).isPresent();

        return userExists && (!userAlreadyInGroup);
    }

    private boolean userCanBeRemoved(int groupId, int userId) {
        boolean userExists = userRepository.isUserExistsById(userId);
        boolean userIsInTheGroup = groupRepository.isUserInGroup(groupId, userId);

        return userExists && userIsInTheGroup;
    }
}