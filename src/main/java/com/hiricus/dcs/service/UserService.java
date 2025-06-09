package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.repository.UserDataRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void changeUserData(UserDataObject userData) {
        if (!userDataRepository.isUserDataExistsById(userData.getId())) {
            throw new EntityNotFoundException("User not found");
        }

        userDataRepository.updateUserData(userData);
    }
}
