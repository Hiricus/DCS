package com.hiricus.dcs.controller;

import com.hiricus.dcs.security.request.UserRegisterRequest;
import com.hiricus.dcs.security.request.UserAuthRequest;
import com.hiricus.dcs.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity login() {
        return new ResponseEntity("Hello", HttpStatus.OK);
    }

    @GetMapping("/verify_token/{token}")
    public ResponseEntity<?> verifyToken(@PathVariable String token) {
        ResponseEntity<?> response = authService.verifyToken(token) ?
                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthRequest loginRequest) {
        String token = authService.loginAndGetToken(loginRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody UserRegisterRequest request) {
        Integer createdUserId = authService.registerNewUser(request).get();
        return new ResponseEntity<>(createdUserId, HttpStatus.OK);
    }
}
