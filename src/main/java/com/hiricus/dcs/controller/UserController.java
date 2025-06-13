package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.ProfileDTO;
import com.hiricus.dcs.dto.UserDataDto;
import com.hiricus.dcs.dto.UserDto;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.security.request.UserRegisterRequest;
import com.hiricus.dcs.service.AuthService;
import com.hiricus.dcs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getOwnProfile(Authentication authentication) {
        String login = authentication.getName();
        UserObject user = userService.getUser(login);
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .get()
                .toString();

        ProfileDTO response = new ProfileDTO(user.getId(), user.getLogin(), role);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
