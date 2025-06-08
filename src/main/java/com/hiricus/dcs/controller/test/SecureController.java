package com.hiricus.dcs.controller.test;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class SecureController {

    @GetMapping("info")
    public String getSecureInfo(Authentication authentication) {
        System.out.println(authentication.getAuthorities());
        return "Hello, " + authentication.getName() + ". Sosal?";
    }
}
