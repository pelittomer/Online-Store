package com.online_store.backend.api.user.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.auth.exception.UserNotFoundException;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtilsService {
    //repositories
    private final UserRepository userRepository;

    public void handleExistingUser(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration attempt for existing email: {}", email);
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email:" + email));
    }
}
