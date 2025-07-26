package com.online_store.backend.api.profile.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProfileMapper {

    public ProfileResponseDto profileMapper(Profile dto) {
        return ProfileResponseDto.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .birthOfDate(dto.getBirthOfDate())
                .gender(dto.getGender())
                .avatar(dto.getAvatar() != null ? dto.getAvatar().getId() : null)
                .build();
    }
}
