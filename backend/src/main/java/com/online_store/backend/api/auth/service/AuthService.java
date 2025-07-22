package com.online_store.backend.api.auth.service;

import java.io.IOException;

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
import com.online_store.backend.api.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.auth.exception.UserNotFoundException;
import com.online_store.backend.api.auth.utils.AuthUtilsService;
import com.online_store.backend.api.profile.entities.Profile;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.common.exception.ApiResponse;
import com.online_store.backend.common.exception.ErrorResponse;
import com.online_store.backend.common.exception.UnauthorizedAccessException;
import com.online_store.backend.modules.jwt.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthUtilsService authUtilsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;

    /**
     * Registers a new user with the specified role.
     * Throws UserAlreadyExistsException if an account with the given email already
     * exists.
     *
     * @param authRequestDto DTO containing user email and password.
     * @param role           The role to assign to the new user
     *                       (e.g.,CUSTOMER,SELLER, ADMIN).
     * @throws UserAlreadyExistsException if the email is already registered.
     */
    @Transactional
    public String register(AuthRequestDto authRequestDto, Role role) {
        Role actualRole = authUtilsService.mapRoleParamToEnum(role);

        if (userRepository.findByEmail(authRequestDto.getEmail()).isPresent()) {
            log.warn("Registration attempt for existing email: {}", authRequestDto.getEmail());
            throw new UserAlreadyExistsException("User with email " + authRequestDto.getEmail() + " already exists.");
        }
        String hashedPassword = authUtilsService.hashedPassword(authRequestDto.getPassword());
        Profile profile = null;
        if (actualRole == Role.CUSTOMER) {
            profile = Profile.builder().build();
        }
        User user = User.builder()
                .email(authRequestDto.getEmail())
                .password(hashedPassword)
                .role(actualRole)
                .build();

        if (profile != null) {
            user.setProfile(profile);
            profile.setUser(user);
        }
        userRepository.save(user);
        
        log.info("User registered successfully: {}", authRequestDto.getEmail());

        return String.format("User %s registered successfully.", authRequestDto.getEmail());
    }

    /**
     * Authenticates a user and generates JWT access and refresh tokens.
     * Sets the refresh token as an HTTP-only cookie.
     *
     * @param authRequestDto DTO containing user email and password.
     * @param response       HttpServletResponse to add the refresh token cookie.
     * @return The access token string.
     * @throws BadCredentialsException     if authentication fails due to incorrect
     *                                     credentials.
     * @throws UserNotFoundException       if the user is not found in the database
     *                                     after authentication
     *                                     (should ideally not happen if
     *                                     UserDetailsService is correctly
     *                                     integrated with your User entity and
     *                                     repository).
     * @throws UnauthorizedAccessException (This throwable is removed from `login`
     *                                     method as role check
     */
    public String login(AuthRequestDto authRequestDto, Role role, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Login failed for email: {}. Reason: Invalid credentials.", authRequestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password!");
        }

        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException("User not found with email:" + authRequestDto.getEmail()));

        if (user.getRole() != role) {
            throw new UnauthorizedAccessException(
                    "User with email " + user.getEmail() + " does not have the required role to perform this action.");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authUtilsService.addRefreshTokenCookie(refreshToken, response);

        log.info("User {} logged in successfully.", user.getEmail());

        return accessToken;
    }

    /**
     * Logs out the user by clearing the refresh token cookie.
     *
     * @param response HttpServletResponse to clear the cookie.
     */
    public String logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);
        log.info("User logged out successfully by clearing refresh token cookie.");

        return "You have successfully logged out.";
    }

    /**
     * Refreshes the access token using a valid refresh token.
     * Assumes refresh token is passed in Authorization header as "Bearer <token>".
     * Writes the new access token directly to the HttpServletResponse output
     * stream.
     *
     * @param request  HttpServletRequest containing the refresh token in the
     *                 header.
     * @param response HttpServletResponse to write the new access token and set
     *                 status.
     * @throws IOException If there's an error writing to the response output
     *                     stream.
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Refresh token request without valid Authorization header.");
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            var accessToken = jwtService.generateToken(userDetails);

            response.setContentType("application/json");
            response.setStatus(HttpStatus.OK.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ApiResponse.builder()
                            .message("")
                            .data(accessToken)
                            .build());
            log.info("Access token refreshed successfully for user: {}", userEmail);
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("An unexpected error occurred during refresh token extraction: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorResponse.builder()
                            .message("An unexpected error occurred: " + e.getMessage())
                            .build());
        }
    }
}