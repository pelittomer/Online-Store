package com.online_store.backend.api.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/{role}/sign-up")
    public String signUp(@PathVariable String role) {
        // Business logic for user registration based on role will be here
        return "User registration for role: " + role + " will be handled here.";
    }

    @PostMapping("/{role}/sign-in")
    public String signIn(@PathVariable String role) {
        // Business logic for user login based on role will be here
        return "User login for role: " + role + " will be handled here.";
    }

    @PostMapping("/sign-out")
    public String signOut() {
        // Business logic for user logout will be here
        return "User logout will be handled here.";
    }

    @GetMapping("/refresh")
    public String refreshToken() {
        // Business logic for token refresh will be here
        return "Token refresh will be handled here.";
    }
}
