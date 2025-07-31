package com.online_store.backend.api.auth.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.service.AuthService;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for user authentication.
 * Manages endpoints for user sign-up, sign-in, sign-out, and token refreshing.
 */
@RestController()
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Endpoint for user registration (sign-up).
     *
     * @param authRequestDto The DTO containing user details for registration.
     * @param role           The role to be assigned to the new user (e.g.,
     *                       CUSTOMER, SELLER).
     * @return A {@link ResponseEntity} with an {@link ApiResponse} indicating
     *         successful registration.
     */
    @PostMapping("/{role}/sign-up")
    public ResponseEntity<ApiResponse<String>> signUp(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            @PathVariable Role role) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        authService.register(authRequestDto, role)));
    }

    /**
     * Endpoint for user login (sign-in).
     * Upon successful authentication, a refresh token is set in an HTTP-only
     * cookie,
     * and an access token is returned in the response body.
     *
     * @param authRequestDto The DTO containing the user's email and password.
     * @param role           The role of the user attempting to sign in.
     * @param response       The {@link HttpServletResponse} to add the refresh
     *                       token cookie.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
     *         JWT access token.
     */
    @PostMapping("/{role}/sign-in")
    public ResponseEntity<ApiResponse<String>> signIn(
            @Valid @RequestBody AuthRequestDto authRequestDto,
            @PathVariable Role role,
            HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success("",
                authService.login(authRequestDto, role, response)));
    }

    /**
     * Endpoint for user logout (sign-out).
     * This method clears the refresh token cookie, effectively logging the user
     * out.
     *
     * @param response The {@link HttpServletResponse} to clear the cookie from.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         logout success message.
     */
    @PostMapping("/sign-out")
    public ResponseEntity<ApiResponse<String>> signOut(HttpServletResponse response) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        authService.logout(response)));
    }

    /**
     * Endpoint to refresh a JWT access token.
     * This endpoint expects a refresh token in the Authorization header.
     * It generates a new access token and writes it directly to the response body.
     *
     * @param request  The {@link HttpServletRequest} containing the refresh token.
     * @param response The {@link HttpServletResponse} to write the new token to.
     * @throws IOException if there is an error writing the response.
     */
    @GetMapping("/refresh")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}