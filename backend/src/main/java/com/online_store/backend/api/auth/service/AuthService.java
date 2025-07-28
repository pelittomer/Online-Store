package com.online_store.backend.api.auth.service;

import java.io.IOException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.utils.AuthUtilsService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.api.user.utils.UserUtilsService;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for handling authentication-related business logic.
 * It provides methods for user registration, login, logout, and token
 * refreshing.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    // utils
    private final AuthUtilsService authUtilsService;
    private final UserUtilsService userUtilsService;
    // services
    private final UserService userService;
    // modules
    private final JwtService jwtService;

    /**
     * Registers a new user with the specified role.
     *
     * @param dto  The DTO containing user registration information (email,
     *             password, etc.).
     * @param role The desired role for the new user.
     * @return A success message indicating the user has been registered.
     * @see com.online_store.backend.api.auth.controller.AuthController#signUp(AuthRequestDto, Role)
     */
    @Transactional
    public String register(AuthRequestDto dto,
            Role role) {
        Role actualRole = authUtilsService.mapRoleParamToEnum(role);

        userService.createUser(dto, actualRole);

        log.info("User registered successfully: {}", dto.getEmail());

        return String.format("User %s registered successfully.", dto.getEmail());
    }

    /**
     * Authenticates a user and generates JWT access and refresh tokens.
     * The refresh token is stored in an HTTP-only cookie.
     *
     * @param dto      The DTO containing the user's login credentials.
     * @param role     The expected role of the user.
     * @param response The {@link HttpServletResponse} to add the refresh token
     *                 cookie to.
     * @return The generated JWT access token.
     * @see com.online_store.backend.api.auth.controller.AuthController#signIn(AuthRequestDto, Role, HttpServletResponse)
     */
    public String login(AuthRequestDto dto,
            Role role,
            HttpServletResponse response) {
        authUtilsService.authenticateUser(dto);

        User user = userUtilsService.findUserByEmail(dto.getEmail());

        authUtilsService.verifyUserRole(user, role);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authUtilsService.addRefreshTokenCookie(refreshToken, response);

        log.info("User {} logged in successfully.", user.getEmail());

        return accessToken;
    }

    /**
     * Logs out the user by clearing the refresh token cookie.
     *
     * @param response The {@link HttpServletResponse} to clear the cookie from.
     * @return A success message indicating the user has been logged out.
     * @see com.online_store.backend.api.auth.controller.AuthController#signOut(HttpServletResponse)
     */
    public String logout(HttpServletResponse response) {
        authUtilsService.clearRefreshTokenCookie(response);

        log.info("User logged out successfully by clearing refresh token cookie.");

        return "You have successfully logged out.";
    }

    /**
     * Refreshes the JWT access token using a valid refresh token from the
     * Authorization header.
     *
     * @param request  The {@link HttpServletRequest} containing the refresh token.
     * @param response The {@link HttpServletResponse} to write the new access token
     *                 to.
     * @throws IOException if an error occurs during writing the response.
     * @see com.online_store.backend.api.auth.controller.AuthController#refreshToken(HttpServletRequest, HttpServletResponse)
     */
    public void refreshToken(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Refresh token request without valid Authorization header.");
            return;
        }

        final String refreshToken = authHeader.substring(7);

        try {
            authUtilsService.handleTokenRefresh(refreshToken, response);
        } catch (UsernameNotFoundException e) {
            authUtilsService.handleUserNotFound(response, e);
        } catch (Exception e) {
            authUtilsService.handleUnexpectedError(response, e);
        }
    }
}