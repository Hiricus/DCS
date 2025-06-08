package com.hiricus.dcs.controller.test;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.user.UserDataObject;
import com.hiricus.dcs.model.repository.UserDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test/userdata")
public class TestControllerUserData {
    private final UserDataRepository userDataRepository;

    public TestControllerUserData(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<UserDataObject> getUserData(@RequestParam("id") Integer userId) {
        Optional<UserDataObject> optionalData = userDataRepository.findUserDataByUserId(userId);

        if (optionalData.isPresent()) {
            return new ResponseEntity<>(optionalData.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("User data with id " + userId + " not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUserData(@RequestBody UserDataObject userData) {
        System.out.println("Creating object: " + userData);
        userDataRepository.createUserData(userData);
        return ResponseEntity.ok("Created");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserData(@RequestBody UserDataObject userData) {
        System.out.println("Updating object: " + userData);
        userDataRepository.updateUserData(userData);
        return ResponseEntity.ok("Updated");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserData(@RequestParam("id") Integer userId) {
        userDataRepository.deleteUserDataByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
