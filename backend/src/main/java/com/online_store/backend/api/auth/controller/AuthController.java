package com.online_store.backend.api.auth.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.service.AuthService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/{role}/sign-up")
    public ResponseEntity<ApiResponse<String>> signUp(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            @PathVariable Role role) {
        return ResponseEntity.ok(
                ApiResponse.success(authService.register(authRequestDto, role)));
    }

    @PostMapping("/{role}/sign-in")
    public ResponseEntity<ApiResponse<String>> signIn(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            @PathVariable Role role,
            HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(
                authService.login(authRequestDto, role, response)));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<String>> signOut(HttpServletResponse response) {
        return ResponseEntity.ok(
                ApiResponse.success(authService.logout(response)));
    }

    @GetMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}