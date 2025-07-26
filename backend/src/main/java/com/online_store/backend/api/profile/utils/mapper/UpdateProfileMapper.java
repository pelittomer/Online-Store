package com.online_store.backend.api.profile.utils.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.entities.Profile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProfileMapper {

    public Profile profileMapper(Profile profile, ProfileRequestDto dto) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(profile::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(profile::setLastName);
        Optional.ofNullable(dto.getBirthOfDate()).ifPresent(profile::setBirthOfDate);
        Optional.ofNullable(dto.getGender()).ifPresent(profile::setGender);
        return profile;
    }
}
