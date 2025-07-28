package com.online_store.backend.api.user.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.profile.service.ProfileService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUserMapper {
    // service
    private final ProfileService profileService;
    // utils
    private final CommonUtilsService commonUtilsService;

    public User userMapper(
            AuthRequestDto dto,
            Role role) {
        Profile profile = profileService.createProfile(role);
        String hashedPassword = commonUtilsService.hashedPassword(dto.getPassword());

        return User.builder()
                .email(dto.getEmail())
                .password(hashedPassword)
                .role(role)
                .profile(profile)
                .build();
    }
}
