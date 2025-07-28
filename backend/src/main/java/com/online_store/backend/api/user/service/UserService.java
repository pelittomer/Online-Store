package com.online_store.backend.api.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.api.user.utils.mapper.CreateUserMapper;
import com.online_store.backend.api.user.utils.mapper.GetUserMapper;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for user-related operations.
 * This service handles retrieving details of the currently authenticated user
 * and creating new user accounts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // repositories
    private final UserRepository userRepository;
    // mappers
    private final CreateUserMapper createUserMapper;
    private final GetUserMapper getUserMapper;

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return a {@link UserResponseDto} containing the user's information.
     * @see com.online_store.backend.api.user.controller.UserController#getActiveUser()
     */
    @Transactional(readOnly = true)
    public UserResponseDto getActiveUser() {
        User user = commonUtilsService.getCurrentUser();

        UserResponseDto userResponse = getUserMapper.userMapper(user);

        log.info("Active user details retrieved successfully for email: {}", userResponse.getEmail());

        return userResponse;
    }

    /**
     * Creates a new user account with a specified role.
     * Before creating the user, it checks if an account with the same email already
     * exists.
     *
     * @param dto  The DTO containing the user's registration details.
     * @param role The role to be assigned to the new user.
     * @see com.online_store.backend.api.auth.service.AuthService#register(AuthRequestDto,
     *      Role)
     */
    @Transactional
    public void createUser(AuthRequestDto dto,
            Role role) {
        handleExistingUser(dto.getEmail());
        User user = createUserMapper.userMapper(dto, role);
        userRepository.save(user);
    }

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to check for existence.
     * @throws UserAlreadyExistsException if a user with the specified email is
     *                                    found.
     * @see com.online_store.backend.api.user.service.UserService#createUser(com.online_store.backend.api.auth.dto.request.AuthRequestDto,
     *      com.online_store.backend.api.user.entities.Role)
     */
    private void handleExistingUser(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration attempt for existing email: {}", email);
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
    }
}
