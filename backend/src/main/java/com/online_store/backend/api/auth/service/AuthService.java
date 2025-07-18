package com.online_store.backend.api.auth.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    
}
