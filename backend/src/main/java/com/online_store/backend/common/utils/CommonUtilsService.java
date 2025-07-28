package com.online_store.backend.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.utils.UserUtilsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonUtilsService {
    // utils
    private final PasswordEncoder passwordEncoder;
    private final UserUtilsService userUtilsService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userUtilsService.findUserByEmail(authentication.getName());
        return user;
    }

    public void checkImageFileType(MultipartFile file) {
        if (!file.getContentType().startsWith("image/")) {
            throw new Error("Invalid file type!");
        }
    }

    public String hashedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
