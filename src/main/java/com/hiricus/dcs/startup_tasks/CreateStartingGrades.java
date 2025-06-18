package com.hiricus.dcs.startup_tasks;

import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.model.repository.GradeRepository;
import com.hiricus.dcs.service.DisciplineService;
import com.hiricus.dcs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Order(3)
public class CreateStartingGrades implements CommandLineRunner {

    private final UserService userService;
    private final DisciplineService disciplineService;
    private final GradeRepository gradeRepository;

    public CreateStartingGrades(UserService userService, DisciplineService disciplineService, GradeRepository gradeRepository) {
        this.userService = userService;
        this.disciplineService = disciplineService;
        this.gradeRepository = gradeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Integer userId1 = userService.getUserDataByFullName("Свиридов", "Павел", "Николаевич").getId();
        if (gradeRepository.findGradesByUserId(userId1).isEmpty()) {
            addGrades();
            log.info("Adding starting grades...");
        }
    }

    private void addGrades() {
        Integer userId1 = userService.getUserDataByFullName("Яровой", "Максим", "Романович").getId();
        Integer userId2 = userService.getUserDataByFullName("Пугач", "Давид", "Сергеевич").getId();
        Integer userId3 = userService.getUserDataByFullName("Свиридов", "Павел", "Николаевич").getId();
        List<Integer> userIds = List.of(userId1, userId2, userId3);

        List<Integer> disciplineIds = disciplineService.getDisciplinesOfUser(userId1).stream()
                .map(DisciplineObject::getId)
                .toList();

        Random random = new Random();
        for (Integer userId : userIds) {
            for (Integer disciplineId : disciplineIds) {
                String grade = String.valueOf(random.nextInt(4, 6));
                FinalGradeObject gradeObject = new FinalGradeObject(grade, LocalDate.now(), userId, disciplineId);
                gradeRepository.createGrade(gradeObject);
            }
        }
    }
}
