package com.hiricus.dcs.startup_tasks;

import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.service.DisciplineService;
import com.hiricus.dcs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Order(2)
public class AddDisciplinesTask implements CommandLineRunner {
    private final DisciplineService disciplineService;
    private final UserService userService;

    @Autowired
    public AddDisciplinesTask(DisciplineService disciplineService,
                              UserService userService) {
        this.disciplineService = disciplineService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (disciplineService.getAllDisciplines().isEmpty()) {
            List<Integer> disciplineIds = createDisciplines();
            addDisciplinesToUsers(disciplineIds);
            log.info("Adding disciplines...");
        }
    }

    public List<Integer> createDisciplines() {
        List<Integer> disciplineIds = new ArrayList<>();
        disciplineIds.add(disciplineService.createDiscipline("Web-разработка"));
        disciplineIds.add(disciplineService.createDiscipline("Математическое моделирование"));
        disciplineIds.add(disciplineService.createDiscipline("Проектирование программных систем"));
        disciplineIds.add(disciplineService.createDiscipline("Многопоточное программирование"));
        disciplineIds.add(disciplineService.createDiscipline("Кроссплатформенная разработка"));
        disciplineIds.add(disciplineService.createDiscipline("Компьютерные сети"));
        disciplineIds.add(disciplineService.createDiscipline("Верификация программных систем"));
        disciplineIds.add(disciplineService.createDiscipline("Системное программирование"));
        return disciplineIds;
    }

    public void addDisciplinesToUsers(List<Integer> disciplineIds) {
        Integer userId1 = userService.getUserDataByFullName("Яровой", "Максим", "Романович").getId();
        Integer userId2 = userService.getUserDataByFullName("Пугач", "Давид", "Сергеевич").getId();
        Integer userId3 = userService.getUserDataByFullName("Свиридов", "Павел", "Николаевич").getId();

        disciplineService.addDisciplinesToUser(userId1, disciplineIds);
        disciplineService.addDisciplinesToUser(userId2, disciplineIds);
        disciplineService.addDisciplinesToUser(userId3, disciplineIds);
    }
}
