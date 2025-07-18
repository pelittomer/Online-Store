package com.online_store.backend.api.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public String addProfileDetails(@RequestBody String details) {
        // Business logic to add new profile details for the user will be here.
        // The 'details' parameter would typically be a DTO (Data Transfer Object)
        // representing the new profile information.
        return "New profile details added: " + details;
    }

    @GetMapping
    public String getProfileDetails() {
        // Business logic to retrieve the active user's profile details will be here.
        return "User profile details will be retrieved here.";
    }

    @PutMapping
    public String updateProfileDetails(@RequestBody String updatedDetails) {
        // Business logic to update the active user's profile details will be here.
        // Similar to POST, 'updatedDetails' would be a DTO with the updated
        // information.
        return "User profile details updated: " + updatedDetails;
    }
}
