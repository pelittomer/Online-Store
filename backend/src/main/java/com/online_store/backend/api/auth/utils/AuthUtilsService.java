package com.online_store.backend.api.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.online_store.backend.api.user.entities.Role;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUtilsService {
    private final PasswordEncoder passwordEncoder;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer refreshExpiration;

    public Role mapRoleParamToEnum(Role role) {
        return role == Role.SELLER ? Role.SELLER : Role.CUSTOMER;
    }

    public String hashedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("jwt", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshExpiration);
        response.addCookie(refreshTokenCookie);
    }
}