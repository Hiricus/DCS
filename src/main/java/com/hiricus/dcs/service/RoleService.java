package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.RoleRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.util.RoleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final RoleContainer roleContainer;

    @Autowired
    public RoleService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       GroupRepository groupRepository,
                       RoleContainer roleContainer) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.roleContainer = roleContainer;
    }

    @Transactional
    public boolean setCurator(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        clearRolesAndSetNew(userId, "ROLE_CURATOR");
        return true;
    }

    // TODO: сделать нормальную логику работы с группами
    @Transactional
    public boolean setHead(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        Optional<GroupObject> optionalGroup = userRepository.getUsersGroup(userId);
        // Если пользователь уже в группе - заменяем текущего старосту на него
        if (optionalGroup.isPresent()) {
            groupRepository.setHead(optionalGroup.get().getId(), userId);
        }

        clearRolesAndSetNew(userId, "ROLE_HEAD");
        return true;
    }

    @Transactional
    public boolean setStudent(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        clearRolesAndSetNew(userId, "ROLE_STUDENT");
        return true;
    }

    private void clearRolesAndSetNew(Integer userId, String role) {
        roleRepository.removeAllRolesFromUser(userId);
        Integer curatorRoleId = roleContainer.getRoleId("role");
        roleRepository.addRoleToUser(userId, curatorRoleId);
    }
}
