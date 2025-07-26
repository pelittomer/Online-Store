package com.online_store.backend.api.user.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetUserMapper {

    public UserResponseDto userMapper(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
