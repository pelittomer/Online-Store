package com.online_store.backend.api.user.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.auth.exception.UserNotFoundException;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for user-related operations.
 * This component provides helper methods for user validation and retrieval
 * with consistent error handling and logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserUtilsService {
    // repositories
    private final UserRepository userRepository;

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to check for existence.
     * @throws UserAlreadyExistsException if a user with the specified email is
     *                                    found.
     * @see com.online_store.backend.api.user.service.UserService#createUser(com.online_store.backend.api.auth.dto.request.AuthRequestDto,
     *      com.online_store.backend.api.user.entities.Role)
     */
    public void handleExistingUser(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration attempt for existing email: {}", email);
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email of the user to be retrieved.
     * @return The {@link User} entity corresponding to the provided email.
     * @throws UserNotFoundException if no user with the given email exists.
     * @see com.online_store.backend.api.auth.service.AuthService#login(com.online_store.backend.api.auth.dto.request.AuthRequestDto,
     *      com.online_store.backend.api.user.entities.Role,
     *      jakarta.servlet.http.HttpServletResponse)
     * @see com.online_store.backend.common.utils.CommonUtilsService#getCurrentUser()
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email:" + email));
    }
}
