package com.aston.authservice.controller;

import com.aston.authservice.entity.User;
import com.aston.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        LOG.info("Registering new user: {}", user.getName());
        String token = authService.register(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        LOG.info("Login attempt for user: {}", user.getName());
        Optional<String> token = authService.authenticate(user.getName(), user.getPassword());

        return token.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    LOG.warn("Invalid login attempt for user: {}", user.getName());
                    return ResponseEntity.status(401).body("Invalid credentials");
                });
    }
}
