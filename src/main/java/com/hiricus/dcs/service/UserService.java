package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.UserDataRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    @Transactional
    public UserDataObject getUserData(int userId) {
        return userDataRepository.findUserDataByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public Integer createUserData(UserDataObject userData) {
        // ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН ИЛИ НЕ ПЕРЕДАН ID СОЗДАЁТСЯ НОВЫЙ АККАУНТ-СТАБ
        if (userData.getId() == null || !(userRepository.isUserExistsById(userData.getId()))) {
            UserObject stubUser = new UserObject("stub email", "stub password");
            int userId = userRepository.createUser(stubUser).get();
            userData.setId(userId);
        }

        return userDataRepository.createUserData(userData).get();
    }

    @Transactional
    public UserObject getUser(String login) {
        Optional<UserObject> user = userRepository.findUserByLogin(login);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("User with login " + login + " not found");
        }

        return user.get();
    }

    @Transactional
    public void changeUserData(UserDataObject userData) {
        if (!userDataRepository.isUserDataExistsById(userData.getId())) {
            throw new EntityNotFoundException("User not found");
        }

        userDataRepository.updateUserData(userData);
    }
}
