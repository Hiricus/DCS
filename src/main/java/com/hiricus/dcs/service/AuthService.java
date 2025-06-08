package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.exception.UserAlreadyExistsException;
import com.hiricus.dcs.model.object.user.RoleObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.RoleRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.security.JwtUtil;
import com.hiricus.dcs.security.data.RegisterRequest;
import com.hiricus.dcs.security.data.UserAuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Optional<Integer> registerNewUser(RegisterRequest request) {
        // Если пользователь с таким логином уже есть
        if (userRepository.isUserExistsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        String hashedPassword = encoder.encode(request.getPassword());
        Optional<Integer> userId = userRepository.createUser(new UserObject(request.getLogin(), hashedPassword));

        // Добавляется дефолтная роль
        // TODO: сделать поиск id роли по имени из БД
        roleRepository.addRoleToUser(userId.get(), 1);

        return userId;
    }

    @Transactional
    public String loginAndGetToken(UserAuthRequest request) {
        String login = request.getLogin();
        Optional<UserObject> userObject = userRepository.findUserByLogin(login);

        // Если пользователя с таким логином нет
        if (userObject.isEmpty()) {
            throw new EntityNotFoundException("Invalid credentials");
        }

        String actualPassword = userObject.get().getPassword();
        String providedPassword = request.getPassword();
        // Сравниваем реальный пароль с переданным
        if (encoder.matches(providedPassword, actualPassword)) {
            // Получаем роли и делаем токен
            List<String> userRoles = roleRepository.getUsersRoles(userObject.get().getId())
                    .stream().map(RoleObject::getRoleName)
                    .toList();
            return jwtUtil.generateToken(login, userRoles);
        } else {
            throw new EntityNotFoundException("Invalid credentials");
        }
    }
}
