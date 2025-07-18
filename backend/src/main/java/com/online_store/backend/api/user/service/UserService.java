package com.online_store.backend.api.user.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
