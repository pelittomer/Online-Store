package com.online_store.backend.api.user.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.common.exception.ApiResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userResponseDto = UserResponseDto.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("getActiveUser should return the authenticated user's details with 200 OK")
    void getActiveUser_ShouldReturnUserDetails_WhenUserIsAuthenticated() {
        when(userService.getActiveUser()).thenReturn(userResponseDto);

        ResponseEntity<ApiResponse<UserResponseDto>> responseEntity = userController.getActiveUser();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<UserResponseDto> apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("", apiResponse.getMessage());
        assertEquals(userResponseDto, apiResponse.getData());

        verify(userService).getActiveUser();
    }
}
