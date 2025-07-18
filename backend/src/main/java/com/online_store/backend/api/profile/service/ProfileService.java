package com.online_store.backend.api.profile.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.profile.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;

}
