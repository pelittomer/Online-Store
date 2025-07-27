package com.online_store.backend.api.favorite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.favorite.dto.request.FavoriteRequestDto;
import com.online_store.backend.api.favorite.dto.response.FavoriteResponseDto;
import com.online_store.backend.api.favorite.service.FavoriteService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing user's favorite products.
 * This controller provides endpoints to add or remove products from a user's
 * favorite list, clear the entire list, and retrieve the list of favorites.
 */
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
        private final FavoriteService favoriteService;

        /**
         * Endpoint to add a product to or remove it from the user's favorite list.
         * This acts as a toggle: if the product is already a favorite, it's removed;
         * otherwise, it's added.
         *
         * @param favoriteRequestDto The DTO containing the ID of the product to
         *                           add/remove.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> addProductToFavorites(
                        @RequestBody FavoriteRequestDto favoriteRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                favoriteService.addProduct(favoriteRequestDto)));
        }

        /**
         * Endpoint to clear all products from the user's favorite list.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @DeleteMapping("/all")
        public ResponseEntity<ApiResponse<String>> clearAllFavorites() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                favoriteService.clearAllFavorites()));
        }

        /**
         * Endpoint to retrieve the list of all favorite products for the current user.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         {@link FavoriteResponseDto}
         *         with the list of favorite products.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<FavoriteResponseDto>> listFavoriteProducts() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                favoriteService.listFavoriteProducts()));
        }
}
