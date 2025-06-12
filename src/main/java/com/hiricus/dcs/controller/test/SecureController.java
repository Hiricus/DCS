package com.hiricus.dcs.controller.test;

import com.hiricus.dcs.security.data.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/admin")
public class SecureController {

    @GetMapping("info")
//    @PreAuthorize("hasRole('ADMIN')")
    public String getSecureInfo(Authentication authentication) {
        System.out.println("Roles: " + authentication.getAuthorities().stream().map(Objects::toString).toList());
        System.out.println("UserId: " + ((CustomUserDetails) authentication.getPrincipal()).getId());
        return "Hello, " + authentication.getName() + ". Sosal?";
    }
}
