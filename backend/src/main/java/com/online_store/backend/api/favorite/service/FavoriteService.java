package com.online_store.backend.api.favorite.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.favorite.dto.request.FavoriteRequestDto;
import com.online_store.backend.api.favorite.dto.response.FavoriteResponseDto;
import com.online_store.backend.api.favorite.entities.Favorite;
import com.online_store.backend.api.favorite.repository.FavoriteRepository;
import com.online_store.backend.api.favorite.utils.mapper.GetFavoritesMapper;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.utils.ProductUtilsService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {
    //repositories
    private final FavoriteRepository favoriteRepository;
    //utils
    private final CommonUtilsService commonUtilsService;
    private final ProductUtilsService productUtilsService;
    //mappers
    private final GetFavoritesMapper getFavoritesMapper;

    @Transactional
    public String addProduct(FavoriteRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        log.info("Attempting to add/remove product ID: {} to/from favorites for user: {}", dto.getProduct(),
                user.getEmail());

        Product product = productUtilsService.findProductById(dto.getProduct());

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseGet(() -> {
                    log.info("No favorite list found for user: {}. Creating a new one.", user.getEmail());
                    return Favorite.builder().user(user).products(new HashSet<>()).build();
                });

        Set<Product> favoriteProducts = favorite.getProducts();

        String message;
        if (favoriteProducts.remove(product)) {
            message = "Product removed from favorites.";
            log.info("Product ID: {} removed from favorites for user: {}", product.getId(), user.getEmail());

        } else {
            favoriteProducts.add(product);
            message = "Product added to favorites.";
            log.info("Product ID: {} added to favorites for user: {}", product.getId(), user.getEmail());
        }

        favoriteRepository.save(favorite);
        return message;
    }

    @Transactional
    public String clearAllFavorites() {
        User user = commonUtilsService.getCurrentUser();
        log.info("Attempting to clear all favorite items for user: {}", user.getEmail());

        return favoriteRepository.findByUser(user)
                .map(favorite -> {
                    favorite.getProducts().clear();
                    favoriteRepository.save(favorite);
                    log.info("All favorite items cleared successfully for user: {}", user.getEmail());
                    return "All favorite items cleared successfully.";
                })
                .orElseGet(() -> {
                    log.warn("No favorite list found to clear for user: {}", user.getEmail());
                    return "No favorite list found to clear for the current user.";
                });
    }

    @Transactional(readOnly = true)
    public FavoriteResponseDto listFavoriteProducts() {
        User user = commonUtilsService.getCurrentUser();
        log.info("Listing favorite products for user: {}", user.getEmail());

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Favorite list not found for the current user."));
        return getFavoritesMapper.favoritesMapper(favorite);
    }

}
