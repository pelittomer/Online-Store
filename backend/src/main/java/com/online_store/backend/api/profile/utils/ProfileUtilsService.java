package com.online_store.backend.api.profile.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileUtilsService {

    public ProfileResponseDto profileResponseMapper(Profile profile) {
        return ProfileResponseDto.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .birthOfDate(profile.getBirthOfDate())
                .gender(profile.getGender())
                .avatar(profile.getAvatar() != null ? profile.getAvatar().getId() : null)
                .build();
    }

    public Profile updateProfileFromDto(Profile profile, ProfileRequestDto dto) {
        if (dto.getFirstName() != null) {
            profile.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            profile.setLastName(dto.getLastName());
        }
        if (dto.getBirthOfDate() != null) {
            profile.setBirthOfDate(dto.getBirthOfDate());
        }
        if (dto.getGender() != null) {
            profile.setGender(dto.getGender());
        }
        return profile;
    }
}
