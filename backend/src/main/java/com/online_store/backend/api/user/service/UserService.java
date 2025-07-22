package com.online_store.backend.api.user.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.utils.UserUtilsService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final CommonUtilsService commonUtilsService;
    private final UserUtilsService userUtilsService;

    /**
     * Retrieves the currently authenticated User entity from Spring Security
     * Context.
     * 
     * @return The currently authenticated User entity.
     */
    public UserResponseDto getActiveUser() {
        User user = commonUtilsService.getCurrentUser();

        UserResponseDto userResponse = userUtilsService.userResponseMapper(user);

        log.info("Active user details retrieved successfully for email: {}", userResponse.getEmail());

        return userResponse;
    }

}
