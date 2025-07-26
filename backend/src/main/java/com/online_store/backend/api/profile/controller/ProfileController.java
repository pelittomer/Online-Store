package com.online_store.backend.api.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.service.ProfileService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfileDetails() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        profileService.getProfileDetails()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateProfileDetails(
            @Valid @RequestPart("profile") ProfileRequestDto profileRequestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        profileService.updateProfileDetails(profileRequestDto, file)));
    }
}