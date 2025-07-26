package com.online_store.backend.api.profile.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileUtilsService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // services
    private final UploadService uploadService;

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
