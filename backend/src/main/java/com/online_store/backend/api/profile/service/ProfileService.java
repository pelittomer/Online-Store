package com.online_store.backend.api.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.profile.dto.request.ProfileRequestDto;
import com.online_store.backend.api.profile.dto.response.ProfileResponseDto;
import com.online_store.backend.api.profile.entities.Gender;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.profile.repository.ProfileRepository;
import com.online_store.backend.api.profile.utils.ProfileUtilsService;
import com.online_store.backend.api.profile.utils.mapper.GetProfileMapper;
import com.online_store.backend.api.profile.utils.mapper.UpdateProfileMapper;
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
    // mappers
    private final GetProfileMapper getProfileMapper;
    private final UpdateProfileMapper updateProfileMapper;

    public ProfileResponseDto getProfileDetails() {
        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);
        return getProfileMapper.profileMapper(profile);
    }

    @Transactional
    public String updateProfileDetails(ProfileRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        Profile profile = profileRepository.findByUser(currentUser);

        profileUtilsService.updateAvatarIfPresent(profile, file);
        updateProfileMapper.profileMapper(profile, dto);
        profileRepository.save(profile);

        log.info("Profile saved successfully for user: {}", currentUser.getEmail());

        return "Profile updated successfully for user.";
    }

    public Profile createProfile(Role role) {
        if (role == Role.CUSTOMER) {
            return Profile.builder()
                    .gender(Gender.PREFER_NOT_TO_SAY)
                    .build();
        }
        return null;
    }
}