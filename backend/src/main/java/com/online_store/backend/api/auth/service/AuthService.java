package com.online_store.backend.api.auth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.service.UserService;
import com.online_store.backend.api.user.utils.UserUtilsService;
import com.online_store.backend.common.exception.ApiResponse;
import com.online_store.backend.common.exception.ErrorResponse;
import com.online_store.backend.common.exception.UnauthorizedAccessException;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.http.Cookie;
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
    private final UserUtilsService userUtilsService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    // services
    private final UserService userService;
    // modules
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer refreshExpiration;

    /**
     * Registers a new user with the specified role.
     *
     * @param dto  The DTO containing user registration information (email,
     *             password, etc.).
     * @param role The desired role for the new user.
     * @return A success message indicating the user has been registered.
     * @see com.online_store.backend.api.auth.controller.AuthController#signUp(AuthRequestDto,
     *      Role)
     */
    @Transactional
    public String register(AuthRequestDto dto,
            Role role) {
        Role actualRole = mapRoleParamToEnum(role);

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
     * @see com.online_store.backend.api.auth.controller.AuthController#signIn(AuthRequestDto,
     *      Role, HttpServletResponse)
     */
    public String login(AuthRequestDto dto,
            Role role,
            HttpServletResponse response) {
        authenticateUser(dto);

        User user = userUtilsService.findUserByEmail(dto.getEmail());

        verifyUserRole(user, role);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        addRefreshTokenCookie(refreshToken, response);

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
        clearRefreshTokenCookie(response);

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
     * @see com.online_store.backend.api.auth.controller.AuthController#refreshToken(HttpServletRequest,
     *      HttpServletResponse)
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
            handleTokenRefresh(refreshToken, response);
        } catch (UsernameNotFoundException e) {
            handleUserNotFound(response, e);
        } catch (Exception e) {
            handleUnexpectedError(response, e);
        }
    }

    /**
     * Maps a given {@link Role} to a valid enum value.
     * This method ensures that the role is either SELLER or CUSTOMER,
     * defaulting to CUSTOMER if the role is not SELLER.
     *
     * @param role The role to map.
     * @return The mapped {@link Role} enum value.
     * @see com.online_store.backend.api.auth.service.AuthService#register(AuthRequestDto,
     *      Role)
     */
    private Role mapRoleParamToEnum(Role role) {
        return role == Role.SELLER ? Role.SELLER : Role.CUSTOMER;
    }

    /**
     * Adds a refresh token as an HTTP-only cookie to the response.
     *
     * @param refreshToken The JWT refresh token to be added.
     * @param response     The {@link HttpServletResponse} to which the cookie will
     *                     be added.
     * @see com.online_store.backend.api.auth.service.AuthService#login(AuthRequestDto,
     *      Role, HttpServletResponse)
     */
    private void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshTokenCookie);
    }

    /**
     * Clears the refresh token cookie from the client's browser.
     *
     * @param response The {@link HttpServletResponse} used to clear the cookie.
     * @see com.online_store.backend.api.auth.service.AuthService#logout(HttpServletResponse)
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param authRequestDto The DTO containing the user's email and password.
     * @throws BadCredentialsException if the email or password is invalid.
     * @see com.online_store.backend.api.auth.service.AuthService#login(AuthRequestDto,
     *      Role, HttpServletResponse)
     */
    private void authenticateUser(AuthRequestDto authRequestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(),
                            authRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Login failed for email: {}. Reason: Invalid credentials.", authRequestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password!");
        }
    }

    /**
     * Verifies if a user has the required role to perform a specific action.
     * An ADMIN user is always authorized.
     *
     * @param user         The user whose role is to be verified.
     * @param requiredRole The role required to perform the action.
     * @throws UnauthorizedAccessException if the user does not have the required
     *                                     role.
     * @see com.online_store.backend.api.auth.service.AuthService#login(AuthRequestDto,
     *      Role, HttpServletResponse)
     */
    private void verifyUserRole(User user, Role requiredRole) {
        if (user.getRole() != Role.ADMIN && user.getRole() != requiredRole) {
            throw new UnauthorizedAccessException(
                    "User with email " + user.getEmail() + " does not have the required role to perform this action.");
        }
    }

    /**
     * Handles the token refresh process by generating a new access token
     * from a valid refresh token.
     *
     * @param refreshToken The refresh token from the cookie.
     * @param response     The {@link HttpServletResponse} to write the new access
     *                     token to.
     * @throws IOException if an error occurs during writing the response.
     * @see com.online_store.backend.api.auth.service.AuthService#refreshToken(jakarta.servlet.http.HttpServletRequest,
     *      HttpServletResponse)
     */
    private void handleTokenRefresh(String refreshToken,
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

    /**
     * Sends a successful API response with the new access token.
     *
     * @param response    The {@link HttpServletResponse} to write the response to.
     * @param accessToken The newly generated access token.
     * @throws IOException if an error occurs during writing the response.
     * @see com.online_store.backend.api.auth.utils.AuthUtilsService#handleTokenRefresh(String,
     *      HttpServletResponse)
     */
    private void sendSuccessResponse(HttpServletResponse response,
            String accessToken) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getOutputStream(),
                ApiResponse.builder().message("").data(accessToken).build());
    }

    /**
     * Handles the case where a user is not found during the refresh token process.
     *
     * @param response The {@link HttpServletResponse} to write the error response
     *                 to.
     * @param e        The {@link UsernameNotFoundException} that was thrown.
     * @throws IOException if an error occurs during writing the response.
     * @see com.online_store.backend.api.auth.service.AuthService#refreshToken(jakarta.servlet.http.HttpServletRequest,
     *      HttpServletResponse)
     */
    private void handleUserNotFound(HttpServletResponse response,
            UsernameNotFoundException e) throws IOException {
        log.error("User not found during refresh token process: {}", e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponse.builder().message(e.getMessage()).build());
    }

    /**
     * Handles unexpected errors that occur during the refresh token process.
     *
     * @param response The {@link HttpServletResponse} to write the error response
     *                 to.
     * @param e        The exception that was thrown.
     * @throws IOException if an error occurs during writing the response.
     * @see com.online_store.backend.api.auth.service.AuthService#refreshToken(jakarta.servlet.http.HttpServletRequest,
     *      HttpServletResponse)
     */
    private void handleUnexpectedError(HttpServletResponse response,
            Exception e) throws IOException {
        log.error("An unexpected error occurred during refresh token: {}", e.getMessage(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponse.builder().message("An unexpected error occurred: " + e.getMessage()).build());
    }
}