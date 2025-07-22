package com.online_store.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.online_store.backend.common.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                // CUSTOMER role required for these POST requests
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/return", // Customer initiates a return
                                                                "/api/review" // Customer posts a review
                                                ).hasRole("CUSTOMER")

                                                // SELLER role required for these PUT requests
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/api/question/**", // Seller answers/updates questions
                                                                "/api/product/**", // Seller updates products
                                                                "/api/return/**" // Seller processes returns
                                                ).hasRole("SELLER")

                                                // SELLER role required for these GET requests
                                                .requestMatchers(HttpMethod.GET,
                                                                "/api/question/**", // Seller views specific questions
                                                                "/api/company/**" // Seller views/manages company details
                                                ).hasRole("SELLER")

                                                // ADMIN role required for these POST requests
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/category", // Admin creates categories
                                                                "/api/brand", // Admin creates brands
                                                                "/api/variation" // Admin creates variations
                                                ).hasRole("ADMIN")

                                                // SELLER role required for these general requests
                                                .requestMatchers(
                                                                "/api/shipper", // Shipper management by seller
                                                                "/api/company" // General company management by seller
                                                ).hasRole("SELLER")

                                                // CUSTOMER role required for these general requests
                                                .requestMatchers(
                                                                "/api/user", // User profile management
                                                                "/api/profile", // Profile details
                                                                "/api/favorite/**", // Favorite products
                                                                "/api/address/**", // Shipping addresses
                                                                "/api/cart/**", // Shopping cart operations
                                                                "/api/order/**", // Order management
                                                                "/api/question", // Customer asks a question
                                                                "/api/payment" // Payment processing
                                                ).hasRole("CUSTOMER")

                                                // PUBLIC access
                                                .requestMatchers(
                                                                "/api/user/auth/**", // Authentication endpoints (login,register)
                                                                "/api/upload/**", // File upload/download (if publicly accessible)
                                                                "/api/category/**", // Publicly viewable categories
                                                                "/api/brand", // Publicly viewable brands
                                                                "/api/variation", // Publicly viewable variations
                                                                "/api/product/**", // Publicly viewable products
                                                                "/api/question/all", // Publicly viewable questions (e.g., FAQs)
                                                                "/api/company", // Publicly viewable company info
                                                                "/api/review" // Publicly viewable reviews
                                                ).permitAll()

                                                // Any other request must be authenticated
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

}
