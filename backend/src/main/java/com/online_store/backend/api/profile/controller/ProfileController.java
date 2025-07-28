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

/**
 * REST controller for managing user profiles.
 * This controller provides endpoints to retrieve and update the profile details
 * of the currently authenticated user.
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
        private final ProfileService profileService;

        /**
         * Endpoint to retrieve the profile details of the current user.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         user's
         *         {@link ProfileResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<ProfileResponseDto>> getProfileDetails() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                profileService.getProfileDetails()));
        }

        /**
         * Endpoint to update the profile details of the current user.
         * This can include updating personal information and/or the profile avatar.
         *
         * @param profileRequestDto The DTO containing the updated profile information.
         * @param file              An optional {@link MultipartFile} for the new
         *                          profile avatar.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PutMapping
        public ResponseEntity<ApiResponse<String>> updateProfileDetails(
                        @Valid @RequestPart("profile") ProfileRequestDto profileRequestDto,
                        @RequestPart(value = "file", required = false) MultipartFile file) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                profileService.updateProfileDetails(profileRequestDto, file)));
        }
}