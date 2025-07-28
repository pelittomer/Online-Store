package com.online_store.backend.api.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for user-related operations.
 * This controller provides an endpoint to retrieve the details of the
 * currently authenticated user.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Endpoint to retrieve the details of the currently authenticated user.
     *
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
     *         {@link UserResponseDto} of the active user.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<UserResponseDto>> getActiveUser() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        userService.getActiveUser()));
    }

}