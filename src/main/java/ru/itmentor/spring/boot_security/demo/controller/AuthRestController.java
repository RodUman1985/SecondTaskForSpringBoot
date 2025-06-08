package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        return ResponseEntity.ok("Logged in successfully as " + authentication.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }
}