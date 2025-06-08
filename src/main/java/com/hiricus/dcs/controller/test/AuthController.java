package com.hiricus.dcs.controller.test;

import com.hiricus.dcs.security.JwtUtil;
import com.hiricus.dcs.security.data.RegisterRequest;
import com.hiricus.dcs.security.data.UserAuthRequest;
import com.hiricus.dcs.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil,
                          AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity login() {
        return new ResponseEntity("Hello", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthRequest loginRequest) {
        String token = authService.loginAndGetToken(loginRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody RegisterRequest request) {
        Integer createdUserId = authService.registerNewUser(request).get();
        return new ResponseEntity<>(createdUserId, HttpStatus.OK);
    }
}
