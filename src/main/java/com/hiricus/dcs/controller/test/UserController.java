package com.hiricus.dcs.controller.test;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.user.UserObject;
import com.hiricus.dcs.model.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<UserObject> getUser(@RequestParam("id") int userId) {
        Optional<UserObject> optionalUser = userRepository.findUserById(userId);

        if (optionalUser.isPresent()) {
            return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserObject user) {
        System.out.println("Creating object: " + user);
        userRepository.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserObject user) {
        System.out.println("Updating object: " + user);
        userRepository.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("id") int userId) {
        userRepository.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
