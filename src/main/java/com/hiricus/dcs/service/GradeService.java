package com.hiricus.dcs.service;

import com.hiricus.dcs.dto.GradeDto;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final DisciplineRepository disciplineRepository;
    private final GroupRepository groupRepository;

    public GradeService(GradeRepository gradeRepository,
                        UserRepository userRepository,
                        UserDataRepository userDataRepository,
                        DisciplineRepository disciplineRepository,
                        GroupRepository groupRepository) {
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
        this.disciplineRepository = disciplineRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public List<FinalGradeObject> getUsersGrades(Integer userId) {
        if (!userRepository.isUserExistsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        // Заполняем название дисциплины для каждой оценки
        List<FinalGradeObject> grades = gradeRepository.findGradesByUserId(userId);
        for (FinalGradeObject grade : grades) {
            grade = fillDisciplineName(grade);
        }
        return grades;
    }

    @Transactional
    public List<FinalGradeObject> getGradesByGroupAndDiscipline(Integer groupId, Integer disciplineId) {
        // Получаем все оценки в группе
        List<FinalGradeObject> allGradesOfGroup = this.getAllGradesOfGroup(groupId);

        // Оставляем только оценки по нужной дисциплине
        List<FinalGradeObject> result = allGradesOfGroup.stream()
                .filter(grade -> grade.getDisciplineId() == disciplineId)
                .toList();

        // Заполняем имя пользователя
        for (FinalGradeObject grade: result) {
            grade = fillUserName(grade);
        }

        return result;
    }

    // ВЫЗЫВАТЬ В ТРАНЗАКЦИИ
    private FinalGradeObject fillDisciplineName(FinalGradeObject grade) {
        Optional<DisciplineObject> discipline = disciplineRepository.findDisciplineById(grade.getDisciplineId());

        if (discipline.isEmpty()) {
            throw new EntityNotFoundException("Discipline with id " + grade.getDisciplineId() + " not found");
        }

        grade.setDisciplineName(discipline.get().getName());
        return grade;
    }

    // ВЫЗЫВАТЬ В ТРАНЗАКЦИИ
    private FinalGradeObject fillUserName(FinalGradeObject grade) {
        Optional<UserDataObject> userData = userDataRepository.findUserDataByUserId(grade.getUserId());

        if (userData.isEmpty()) {
            throw new EntityNotFoundException("User with id " + grade.getUserId() + " not found");
        }

        grade.setUserName(userData.get().getName());
        return grade;
    }

    // ВЫЗЫВАТЬ В ТРАНЗАКЦИИ
    private List<FinalGradeObject> getAllGradesOfGroup(Integer groupId) {
        if (!groupRepository.isGroupExistsById(groupId)) {
            throw new EntityNotFoundException("Group not found");
        }

        List<Integer> memberIds = groupRepository.getGroupMembers(groupId)
                .stream()
                .map(UserObject::getId)
                .toList();

        // Находим все оценки всех членов группы
        List<FinalGradeObject> allGrades = new ArrayList<>();
        for (Integer memberId : memberIds) {
            allGrades.addAll(gradeRepository.findGradesByUserId(memberId));
        }

        return allGrades;
    }
}
