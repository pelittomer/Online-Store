package com.online_store.backend.api.profile.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

/**
 * Utility service for managing user profile-related tasks.
 * This class provides helper methods, particularly for handling profile picture
 * (avatar) uploads.
 */
@Component
@RequiredArgsConstructor
public class ProfileUtilsService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // services
    private final UploadService uploadService;

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
    public void updateAvatarIfPresent(Profile profile, MultipartFile file) {
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
