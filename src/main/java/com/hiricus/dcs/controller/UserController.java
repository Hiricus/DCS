package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.UserDataDto;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.security.request.UserRegisterRequest;
import com.hiricus.dcs.service.AuthService;
import com.hiricus.dcs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService,
                          AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    // Создание нового пользователя администратором
    @PostMapping
    public ResponseEntity<?> createUser(UserRegisterRequest request) {
        Integer createdUserId = authService.registerNewUser(request).get();
        return new ResponseEntity<>(createdUserId, HttpStatus.CREATED);
    }

    // Получение личных данных пользователя по id
    @GetMapping("/data/{id}")
    public ResponseEntity<UserDataDto> getUserData(@PathVariable Integer id) {
        UserDataObject userData = userService.getUserData(id);
        UserDataDto userDto = new UserDataDto(userData);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    // Изменение личных данных пользователя по id
    // TODO: сделать чтобы менялись только переданные поля
    @PatchMapping("/data/{id}")
    public ResponseEntity<?> changeUserData(@PathVariable Integer id, @RequestBody UserDataDto userData) {
        userService.changeUserData(new UserDataObject(id, userData));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
