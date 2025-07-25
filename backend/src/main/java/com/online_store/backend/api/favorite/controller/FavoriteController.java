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

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addProductToFavorites(
        @RequestBody FavoriteRequestDto favoriteRequestDto
        ) {
        return ResponseEntity.ok(
                ApiResponse.success(favoriteService.addProduct(favoriteRequestDto)));
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse<String>> clearAllFavorites() {
        return ResponseEntity.ok(
                ApiResponse.success(favoriteService.clearAllFavorites()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FavoriteResponseDto>> listFavoriteProducts() {
        return ResponseEntity.ok(
                ApiResponse.success(favoriteService.listFavoriteProducts()));
    }
}
