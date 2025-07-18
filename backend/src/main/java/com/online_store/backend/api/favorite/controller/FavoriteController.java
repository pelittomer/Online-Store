package com.online_store.backend.api.favorite.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public String addProductToFavorites(@RequestBody String productId) {
        // Business logic to add a product to the user's favorites will be here.
        // 'productId' would typically come from the request body.
        return "Product with ID " + productId + " added to favorites.";
    }

    @DeleteMapping("/{id}")
    public String removeProductFromFavorites(@PathVariable String id) {
        // Business logic to remove a specific product by its ID from favorites will be
        // here.
        return "Product with ID " + id + " removed from favorites.";
    }

    @DeleteMapping("/all")
    public String clearAllFavorites() {
        // Business logic to remove all products from the user's favorites will be here.
        return "All favorite products cleared.";
    }

    @GetMapping
    public String listFavoriteProducts() {
        // Business logic to retrieve and list all favorite products for the user will
        // be here.
        return "List of favorite products for the user will be returned here.";
    }
}
