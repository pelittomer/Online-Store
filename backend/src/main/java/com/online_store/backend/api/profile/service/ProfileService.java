package com.online_store.backend.api.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.auth.exception.UserNotFoundException;
import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.profile.repository.ProfileRepository;
import com.online_store.backend.api.profile.utils.ProfileUtilsService;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    // utils
    private final CommonUtilsService commonUtilsService;
    private final ProfileUtilsService profileUtilsService;
    // repositories
    private final ProfileRepository profileRepository;
    // services
    private final UploadService uploadService;

    /**
     * Retrieves the profile details of the currently authenticated user.
     *
     * @return ProfileResponseDto with the current user's profile details.
     * @throws UserNotFoundException if no authenticated user is found in the
     *                               security context.
     */
    public ProfileResponseDto getProfileDetails() {
        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);

        ProfileResponseDto profileResponse = profileUtilsService.profileResponseMapper(profile);

        return profileResponse;
    }

    /**
     * Updates the profile details of the currently authenticated user.
     * Includes handling for avatar (image) file upload or update.
     *
     * @param profileRequestDto DTO containing the updated profile details.
     * @param file              Optional MultipartFile for the new avatar image. Can
     *                          be null or empty.
     * @return ProfileResponseDto with the updated profile details.
     * 
     */
    @Transactional
    public String updateProfileDetails(ProfileRequestDto profileRequestDto, MultipartFile file) {

        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);

        if (file != null && !file.isEmpty()) {
            commonUtilsService.checkImageFileType(file);

            if (profile.getAvatar() != null) {
                uploadService.updateExistingUploadContent(profile.getAvatar(), file);
            } else {
                Upload newAvatar = uploadService.createFile(file);
                profile.setAvatar(newAvatar);
            }
        }
        profileUtilsService.updateProfileFromDto(profile, profileRequestDto);
        profileRepository.save(profile);

        log.info("Profile saved successfully for user: {}", currentUser.getEmail());

        return "Profile updated successfully for user.";
    }

    public Profile createProfile(Role role) {
        if (role == Role.CUSTOMER) {
            return Profile.builder().build();
        }
        return null;
    }
}