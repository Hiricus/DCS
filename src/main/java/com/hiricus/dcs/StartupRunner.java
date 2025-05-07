package com.hiricus.dcs;

import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import com.hiricus.dcs.model.object.discipline.FinalGradeObject;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.task.TaskObject;
import com.hiricus.dcs.model.object.task.TaskType;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class StartupRunner implements CommandLineRunner {
    RoleRepository roleRepository;
    GroupRepository groupRepository;
    DisciplineRepository disciplineRepository;
    UserRepository userRepository;
    GradeRepository gradeRepository;
    DocumentRepository documentRepository;
    TaskRepository taskRepository;

    public StartupRunner(RoleRepository roleRepository, GroupRepository groupRepository, DisciplineRepository disciplineRepository, UserRepository userRepository, GradeRepository gradeRepository, DocumentRepository documentRepository, TaskRepository taskRepository) {
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.disciplineRepository = disciplineRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.documentRepository = documentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Приложение запущено!!!");
        FinalGradeObject dva = new FinalGradeObject("Не толково", LocalDate.now(), 1, 0);
        FinalGradeObject tri = new FinalGradeObject("Толково", LocalDate.now(), 1, 3);
        FinalGradeObject norm = new FinalGradeObject("Пофиг", LocalDate.now(), 4, 0);

        GroupObject groupObject = new GroupObject(4, "ПИ-212", 2, 2025);


//        List<UserObject> subjects = List.of(
//                new UserObject(1, "", "", "", LocalDateTime.now()),
//                new UserObject(4, "", "", "", LocalDateTime.now())
//        );
//        taskRepository.addTaskSubjects(2, subjects);

//        taskRepository.removeAllTaskSubjects(2);
//        List<UserObject> assignedToTask = taskRepository.getTaskSubjects(1);
//        for (UserObject userObject : assignedToTask) {
//            System.out.println(userObject);
//        }
    }
}