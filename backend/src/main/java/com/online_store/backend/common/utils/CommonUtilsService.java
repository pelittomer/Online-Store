package com.online_store.backend.common.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonUtilsService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return user;
    }

    public void checkImageFileType(MultipartFile file) {
        if (!file.getContentType().startsWith("image/")) {
            throw new Error("Invalid file type!");
        }
    }
}
