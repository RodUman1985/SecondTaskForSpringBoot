package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.spring.boot_security.demo.model.User;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserRestController {
    @GetMapping
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }
}