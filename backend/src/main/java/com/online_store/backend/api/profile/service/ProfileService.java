package com.online_store.backend.api.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Gender;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.profile.repository.ProfileRepository;
import com.online_store.backend.api.profile.utils.mapper.GetProfileMapper;
import com.online_store.backend.api.profile.utils.mapper.UpdateProfileMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing user profiles.
 * This service handles the business logic for creating, retrieving, and
 * updating user profiles,
 * including profile details and avatar images.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // repositories
    private final ProfileRepository profileRepository;
    // mappers
    private final GetProfileMapper getProfileMapper;
    private final UpdateProfileMapper updateProfileMapper;
    // services
    private final UploadService uploadService;

    /**
     * Retrieves the profile details for the currently authenticated user.
     *
     * @return A {@link ProfileResponseDto} containing the user's profile
     *         information.
     * @see com.online_store.backend.api.profile.controller.ProfileController#getProfileDetails()
     */
    public ProfileResponseDto getProfileDetails() {
        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);
        return getProfileMapper.profileMapper(profile);
    }

    /**
     * Updates the profile details for the current user.
     * This method handles updating the user's personal information and,
     * if provided, their profile avatar.
     *
     * @param dto  The DTO containing the updated profile details.
     * @param file The new avatar image file, which can be null.
     * @return A success message upon a successful profile update.
     * @see com.online_store.backend.api.profile.controller.ProfileController#updateProfileDetails(ProfileRequestDto,
     *      MultipartFile)
     */
    @Transactional
    public String updateProfileDetails(ProfileRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);

        updateAvatarIfPresent(profile, file);
        updateProfileMapper.profileMapper(profile, dto);
        profileRepository.save(profile);

        log.info("Profile saved successfully for user: {}", currentUser.getEmail());

        return "Profile updated successfully for user.";
    }

    /**
     * Creates a default profile for a new user based on their role.
     * Currently, it creates a profile for a customer with a default gender.
     *
     * @param role The role of the new user.
     * @return A new {@link Profile} entity with default values, or null if the role
     *         is not supported.
     * @see com.online_store.backend.api.user.utils.mapper.CreateUserMapper#userMapper(com.online_store.backend.api.auth.dto.request.AuthRequestDto,
     *      Role)
     */
    public Profile createProfile(Role role) {
        if (role == Role.CUSTOMER) {
            return Profile.builder()
                    .gender(Gender.PREFER_NOT_TO_SAY)
                    .build();
        }
        return null;
    }

    /**
     * Updates the user's profile avatar if a new file is provided.
     * This method first validates the file type. If the user already has an avatar,
     * it updates the existing upload. If not, it creates a new upload and sets it
     * as the new avatar.
     *
     * @param profile The {@link Profile} entity of the user.
     * @param file    The new avatar file, which can be {@code null} or empty if no
     *                update is needed.
     * @see com.online_store.backend.api.profile.service.ProfileService#updateProfileDetails(com.online_store.backend.api.profile.dto.request.ProfileRequestDto,
     *      MultipartFile)
     */
    private void updateAvatarIfPresent(Profile profile, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            commonUtilsService.checkImageFileType(file);

            if (profile.getAvatar() != null) {
                uploadService.updateExistingUploadContent(profile.getAvatar(), file);
            } else {
                Upload newAvatar = uploadService.createFile(file);
                profile.setAvatar(newAvatar);
            }
        }
    }
}