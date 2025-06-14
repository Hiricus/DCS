package com.hiricus.dcs;

import com.hiricus.dcs.dto.request.GroupCreationRequest;
import com.hiricus.dcs.model.object.group.GroupObject;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.security.request.UserRegisterRequest;
import com.hiricus.dcs.service.AuthService;
import com.hiricus.dcs.service.GroupService;
import com.hiricus.dcs.service.RoleService;
import com.hiricus.dcs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AddAdminUsersTask implements CommandLineRunner {
    private final UserRepository userRepository;

    private final AuthService authService;
    private final UserService userService;
    private final GroupService groupService;
    private final RoleService roleService;


    public AddAdminUsersTask(UserRepository userRepository,
                             AuthService authService,
                             UserService userService,
                             GroupService groupService, RoleService roleService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.userService = userService;
        this.groupService = groupService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!userRepository.isUserExistsByLogin("Hiricus")) {
            log.info("Adding system users...");
            authService.addAdminUser("Hiricus", "2556145");
            authService.addAdminUser("admin", "admin");

            // Не админы
            authService.registerNewUser(new UserRegisterRequest("maksim", "her@yandex.sosu", "maksim"));

            // Добавляем тестовые группы
            createTestGroups();
        }
    }

    @Transactional
    public void createTestGroups() {
        // Создаём аккаунты для старост
        UserRegisterRequest headRegistry1 = new UserRegisterRequest("head 1", "sas_i_sos@mail.ru", "123");
        Integer headId1 = authService.registerNewUser(headRegistry1).get();

        UserRegisterRequest headRegistry2 = new UserRegisterRequest("head 2", "sos_i_sas@mail.ru", "123");
        Integer headId2 = authService.registerNewUser(headRegistry2).get();

        // Создаём студентов
        UserDataObject data1 = new UserDataObject("Павел", "Свиридов", "Николаевич", LocalDateTime.now(), "+79781149146", "123456 7890", "idk lol");
        UserDataObject data2 = new UserDataObject("Максим", "Яровой", "Романович", LocalDateTime.now(), "+7978123441", "123456 7890", "idk lol");
        UserDataObject data3 = new UserDataObject(headId1, "Давид", "Пугач", "Сергеевич", LocalDateTime.now(), "+79781234432", "123456 7890", "idk lol");
        UserDataObject data4 = new UserDataObject("Даниил", "Погонялов", "Дмитриевич", LocalDateTime.now(), "+79788667056", "123456 7890", "idk lol");
        UserDataObject data5 = new UserDataObject("Андрей", "Саенко", "Алексеевич", LocalDateTime.now(), "+79787271488", "123456 7890", "idk lol");
        UserDataObject data6 = new UserDataObject(headId2, "Баhleдун", "V", "Мужеложец", LocalDateTime.now(), "+88005553535", "123456 1337", "idk lol");

        Integer userId1 = userService.createUserData(data1);
        Integer userId2 = userService.createUserData(data2);
        Integer userId3 = userService.createUserData(data3);
        Integer userId4 = userService.createUserData(data4);
        Integer userId5 = userService.createUserData(data5);
        Integer userId6 = userService.createUserData(data6);


        // Создаём куратора
        UserRegisterRequest curatorRegistry = new UserRegisterRequest("Curator 228", "idk@mail.ru", "123");
        Integer curatorId = authService.registerNewUser(curatorRegistry).get();
        roleService.setCurator(curatorId);

        // Назначаем роли старостам
        roleService.setHead(userId3);
        roleService.setHead(userId6);

        // Создаём группы
        GroupCreationRequest group1 = new GroupCreationRequest("ПИ-212", curatorId, userId3, 4, 2021);
        GroupCreationRequest group2 = new GroupCreationRequest("ПИ-211", curatorId, userId6, 4, 2021);

        Integer groupId1 = groupService.createGroup(new GroupObject(group1));
        Integer groupId2 = groupService.createGroup(new GroupObject(group2));

        // Добавляем в группы пользователей
        groupService.addUsersToGroup(groupId1, List.of(userId1, userId2, userId3));
        groupService.addUsersToGroup(groupId2, List.of(userId4, userId5, userId6));
    }
}
