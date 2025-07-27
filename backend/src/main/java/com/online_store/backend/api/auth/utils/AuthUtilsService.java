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

/**
 * A utility service class for authentication-related operations.
 * This class provides helper methods for user authentication, role management,
 * JWT handling (cookies, token refresh), and exception handling for API
 * responses.
 * 
 */
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
    public Role mapRoleParamToEnum(Role role) {
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
    public void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
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
    public void verifyUserRole(User user, Role requiredRole) {
        if (user.getRole() != Role.ADMIN && user.getRole() != requiredRole) {
            throw new UnauthorizedAccessException(
                    "User with email " + user.getEmail() + " does not have the required role to perform this action.");
        }
    }

    /**
     * Clears the refresh token cookie from the client's browser.
     *
     * @param response The {@link HttpServletResponse} used to clear the cookie.
     * @see com.online_store.backend.api.auth.service.AuthService#logout(HttpServletResponse)
     */
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
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
    public void handleUserNotFound(HttpServletResponse response,
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
    public void handleUnexpectedError(HttpServletResponse response,
            Exception e) throws IOException {
        log.error("An unexpected error occurred during refresh token: {}", e.getMessage(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        objectMapper.writeValue(response.getOutputStream(),
                ErrorResponse.builder().message("An unexpected error occurred: " + e.getMessage()).build());
    }
}