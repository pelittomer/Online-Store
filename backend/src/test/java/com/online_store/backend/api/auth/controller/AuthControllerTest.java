package com.online_store.backend.api.auth.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.service.AuthService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    private AuthRequestDto authRequestDto;

    @BeforeEach
    void setUp() {
        authRequestDto = AuthRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    @DisplayName("Sign-up should call authService.register and return success message")
    void signUp_ShouldReturnOk_WithSuccessMessage() {
        String token = "accessToken";
        when(authService.register(any(AuthRequestDto.class), eq(Role.CUSTOMER)))
                .thenReturn(token);

        ResponseEntity<ApiResponse<String>> responseEntity = authController.signUp(authRequestDto, Role.CUSTOMER);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody().getMessage());
        assertEquals(token, responseEntity.getBody().getData());
        verify(authService).register(any(AuthRequestDto.class), eq(Role.CUSTOMER));
    }

    @Test
    @DisplayName("Sign-in should call authService.login and return JWT access token")
    void signIn_ShouldReturnOk_WithJwtToken() {
        String accessToken = "accessToken";
        when(authService.login(any(AuthRequestDto.class), eq(Role.CUSTOMER), eq(response)))
                .thenReturn(accessToken);

        ResponseEntity<ApiResponse<String>> responseEntity = authController.signIn(authRequestDto, Role.CUSTOMER,
                response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody().getMessage());
        assertEquals(accessToken, responseEntity.getBody().getData());
        verify(authService).login(any(AuthRequestDto.class), eq(Role.CUSTOMER), eq(response));
    }

    @Test
    @DisplayName("Sign-out should call authService.logout and return success message")
    void signOut_ShouldReturnOk_WithSuccessMessage() {
        String logoutMessage = "You have been logged out successfully.";
        when(authService.logout(eq(response))).thenReturn(logoutMessage);

        ResponseEntity<ApiResponse<String>> responseEntity = authController.signOut(response);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("", responseEntity.getBody().getMessage());
        assertEquals(logoutMessage, responseEntity.getBody().getData());
        verify(authService).logout(eq(response));
    }

    @Test
    @DisplayName("Refresh token should call authService.refreshToken")
    void refreshToken_ShouldCallAuthServiceRefreshToken() throws IOException {
        authController.refreshToken(request, response);

        verify(authService).refreshToken(eq(request), eq(response));
    }
}