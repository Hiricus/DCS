package com.hiricus.dcs.service;

import com.hiricus.dcs.dto.DisciplineDto;
import com.hiricus.dcs.exception.EntityAlreadyExistsException;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.DisciplineRepository;
import com.hiricus.dcs.model.repository.GroupRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public DisciplineService(DisciplineRepository disciplineRepository,
                             GroupRepository groupRepository,
                             UserRepository userRepository) {
        this.disciplineRepository = disciplineRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<DisciplineObject> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    @Transactional
    public List<DisciplineObject> getDisciplinesOfGroup(Integer groupId) {
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group not found");
        }

        List<Integer> userIds = groupRepository.getGroupMembers(groupId)
                .stream()
                .map(UserObject::getId)
                .toList();

        Set<DisciplineObject> allDisciplines = new HashSet<>();
        for (Integer userId : userIds) {
            List<DisciplineObject> userDisciplines = userRepository.getUsersDisciplines(userId);
            allDisciplines.addAll(userDisciplines);
        }

        return allDisciplines.stream().toList();
    }

    @Transactional
    public List<DisciplineObject> getDisciplinesOfUser(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        return userRepository.getUsersDisciplines(userId);
    }

    // TODO: сделать проверку что все дисциплины существуют
    @Transactional
    public Integer addDisciplinesToUser(Integer userId, List<Integer> disciplineIds) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return disciplineRepository.addDisciplinesToUser(userId, disciplineIds);
    }

    @Transactional
    public Integer createDiscipline(String name) {
        if (disciplineRepository.isExistsByName(name)) {
            throw new EntityAlreadyExistsException("Discipline with name: " + name + " already exists");
        }

        return disciplineRepository.createEmptyDiscipline(new DisciplineObject(name)).get();
    }

    @Transactional
    public Integer deleteDiscipline(Integer id) {
        return disciplineRepository.deleteDisciplineById(id);
    }
}
