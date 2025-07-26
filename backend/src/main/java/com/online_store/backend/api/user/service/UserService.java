package com.online_store.backend.api.user.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.api.user.utils.UserUtilsService;
import com.online_store.backend.api.user.utils.mapper.CreateUserMapper;
import com.online_store.backend.api.user.utils.mapper.GetUserMapper;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // services
    private final UserUtilsService userUtilsService;
    // repositories
    private final UserRepository userRepository;
    // mappers
    private final CreateUserMapper createUserMapper;
    private final GetUserMapper getUserMapper;

    public UserResponseDto getActiveUser() {
        User user = commonUtilsService.getCurrentUser();

        UserResponseDto userResponse = getUserMapper.userMapper(user);

        log.info("Active user details retrieved successfully for email: {}", userResponse.getEmail());

        return userResponse;
    }

    public void createUser(AuthRequestDto dto,
            Role role) {
        userUtilsService.handleExistingUser(dto.getEmail());
        User user = createUserMapper.userMapper(dto, role);
        userRepository.save(user);
    }
}
