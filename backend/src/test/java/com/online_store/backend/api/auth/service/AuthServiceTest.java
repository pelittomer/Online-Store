package com.online_store.backend.api.auth.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.api.user.utils.UserUtilsService;
import com.online_store.backend.common.exception.UnauthorizedAccessException;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
public class AuthServiceTest {

    @Mock
    private UserUtilsService userUtilsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthService authService;

    private AuthRequestDto authRequestDto;
    private User user;
    private HttpServletResponse response;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() throws IOException {
        authRequestDto = AuthRequestDto.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .build();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        ReflectionTestUtils.setField(authService, "refreshExpiration", 3600);
    }

    @Test
    @DisplayName("Registration should succeed and return success message")
    void register_ShouldSuccessed_AndReturnSuccessMessage() {
        doNothing().when(userService).createUser(authRequestDto, Role.CUSTOMER);

        String result = authService.register(authRequestDto, Role.CUSTOMER);
        assertNotNull(result);

        assertTrue(result.contains("registered successfully"));
        verify(userService, times(1)).createUser(any(AuthRequestDto.class), eq(Role.CUSTOMER));
    }

    @Test
    @DisplayName("Login should succeed and return access token")
    void login_ShouldSucceed_AndReturnAccessToken() {
        String accessToken = "mockAccessToken";
        String refreshToken = "mockRefreshToken";

        when(userUtilsService.findUserByEmail(authRequestDto.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        String result = authService.login(authRequestDto, Role.CUSTOMER, response);

        assertEquals(accessToken, result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("Login should fail with bad credentials and throw BadCredentialsException")
    void login_ShouldFail_WithBadCredentials_AndThrowBadCredentialsException() {
        doThrow(BadCredentialsException.class).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.login(authRequestDto, Role.CUSTOMER, response));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Login should fail with different role and throw UnauthorizedAccessException")
    void login_ShouldFail_WithDifferentRole_AndThrowUnauthorizedAccessException() {
        User sellerUser = User.builder()
                .email("seller@example.com")
                .password("encodedPassword")
                .role(Role.SELLER)
                .build();

        when(userUtilsService.findUserByEmail(authRequestDto.getEmail())).thenReturn(sellerUser);

        assertThrows(UnauthorizedAccessException.class,
                () -> authService.login(authRequestDto, Role.CUSTOMER, response));
    }

    @Test
    @DisplayName("Logout should succeed and return success message")
    void logout_ShouldSucceed_AndReturnSuccessMessage() {
        String result = authService.logout(response);

        assertNotNull(result);
        assertTrue(result.contains("logged out"));
        verify(response, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("Refresh token should succeed and return new access token")
    void refreshToken_ShouldSucceed_AndReturnNewAccessToken() throws IOException {
        String refreshToken = "mockRefreshToken";
        String userEmail = "test@example.com";
        String newAccessToken = "newMockAccessToken";
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(userEmail)
                .password("encodedPassword")
                .authorities(Role.CUSTOMER.name())
                .build();

        // objectMapper.writeValue metoduna bir stub ekleyin
        doNothing().when(objectMapper).writeValue(any(ServletOutputStream.class), any());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn(newAccessToken);

        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        });

        authService.refreshToken(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(200);

        // ObjectMapper.writeValue metodunun çağrıldığını doğrulayın.
        verify(objectMapper).writeValue(any(ServletOutputStream.class), any());

        verify(response, times(1)).getOutputStream();
    }

    @Test
    @DisplayName("Refresh token should fail with an invalid token")
    void refreshToken_ShouldFail_WithInvalidToken() throws IOException {
        String refreshToken = "invalidRefreshToken";
        String userEmail = "test@example.com";
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userEmail)
                .password("encodedPassword")
                .authorities(Role.CUSTOMER.name())
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(false);

        authService.refreshToken(request, response);

        verify(response).setStatus(401);
        verify(response, never()).getOutputStream();
    }

    @Test
    @DisplayName("Refresh token should fail without an Authorization header")
    void refreshToken_ShouldFail_WithoutAuthorizationHeader() throws IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authService.refreshToken(request, response);

        verify(jwtService, never()).extractUsername(anyString());
        verify(response, never()).getOutputStream();
    }

    @Test
    @DisplayName("Refresh token should fail when user is not found")
    void refreshToken_ShouldFail_WhenUserNotFound() throws IOException {
        String refreshToken = "mockRefreshToken";
        String userEmail = "nonexistent@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail))
                .thenThrow(new UsernameNotFoundException("User not found"));

        authService.refreshToken(request, response);

        verify(response).setStatus(404);
        verify(response).getOutputStream();
    }
}
