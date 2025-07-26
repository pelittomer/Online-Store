package com.online_store.backend.api.auth.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.exception.ApiResponse;
import com.online_store.backend.common.exception.ErrorResponse;
import com.online_store.backend.common.exception.UnauthorizedAccessException;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUtilsService {
    // utils
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    // modules
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer refreshExpiration;

    public Role mapRoleParamToEnum(Role role) {
        return role == Role.SELLER ? Role.SELLER : Role.CUSTOMER;
    }

    public void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshTokenCookie);
    }

    public void authenticateUser(AuthRequestDto authRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(),
                            authRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Login failed for email: {}. Reason: Invalid credentials.", authRequestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password!");
        }
    }

    public void verifyUserRole(User user, Role requiredRole) {
        if (user.getRole() != Role.ADMIN && user.getRole() != requiredRole) {
            throw new UnauthorizedAccessException(
                    "User with email " + user.getEmail() + " does not have the required role to perform this action.");
        }
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }

    public void handleTokenRefresh(String refreshToken,
            HttpServletResponse response) throws IOException {
        final String userEmail = jwtService.extractUsername(refreshToken);

        // Guard Clause: Kullanıcı e-postası kontrolü
        if (userEmail == null || userEmail.trim().isEmpty()) {
            log.warn("Refresh token request with invalid user email.");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // Guard Clause: Token geçerliliği kontrolü
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            log.warn("Invalid refresh token for user: {}", userEmail);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        var accessToken = jwtService.generateToken(userDetails);
        sendSuccessResponse(response, accessToken);
        log.info("Access token refreshed successfully for user: {}", userEmail);
    }

    private void sendSuccessResponse(HttpServletResponse response,
            String accessToken) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getOutputStream(),
                ApiResponse.builder().message("").data(accessToken).build());
    }

    public void handleUserNotFound(HttpServletResponse response,
            UsernameNotFoundException e) throws IOException {
        log.error("User not found during refresh token process: {}", e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponse.builder().message(e.getMessage()).build());
    }

    public void handleUnexpectedError(HttpServletResponse response,
            Exception e) throws IOException {
        log.error("An unexpected error occurred during refresh token: {}", e.getMessage(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponse.builder().message("An unexpected error occurred: " + e.getMessage()).build());
    }
}