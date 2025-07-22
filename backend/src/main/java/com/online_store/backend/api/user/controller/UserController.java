package com.online_store.backend.api.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<UserResponseDto>> getActiveUser() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        userService.getActiveUser()));
    }

}