package com.online_store.backend.api.favorite.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.favorite.dto.request.FavoriteRequestDto;
import com.online_store.backend.api.favorite.dto.response.FavoriteResponseDto;
import com.online_store.backend.api.favorite.entities.Favorite;
import com.online_store.backend.api.favorite.repository.FavoriteRepository;
import com.online_store.backend.api.favorite.utils.mapper.GetFavoritesMapper;
import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final CommonUtilsService commonUtilsService;
    private final ProductRepository productRepository;
    private final GetFavoritesMapper getFavoritesMapper;

    public String addProduct(FavoriteRequestDto favoriteRequestDto) {
        User user = commonUtilsService.getCurrentUser();

        Product product = productRepository.findById(favoriteRequestDto.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

        Favorite favorite = favoriteRepository.findByUser(user)
                .orElseGet(() -> Favorite.builder().user(user).products(new HashSet<>()).build());

        Set<Product> favoriteProducts = favorite.getProducts();

        String message;
        if (favoriteProducts.contains(product)) {
            favoriteProducts.remove(product);
            message = "Product removed from favorites.";
        } else {
            favoriteProducts.add(product);
            message = "Product added to favorites.";
        }

        favoriteRepository.save(favorite);

        return message;
    }

    public String clearAllFavorites() {
        User user = commonUtilsService.getCurrentUser();
        Optional<Favorite> optionalFavorite = favoriteRepository.findByUser(user);
        if (optionalFavorite.isEmpty()) {
            return "No favorite list found to clear for the current user.";
        }

        Favorite favorite = optionalFavorite.get();
        favorite.getProducts().clear();

        favoriteRepository.save(favorite);

        return "All favorite items cleared successfully.";
    }

    public FavoriteResponseDto listFavoriteProducts() {
        User user = commonUtilsService.getCurrentUser();
        Optional<Favorite> optionalFavorite = favoriteRepository.findByUser(user);
        Favorite favorite = optionalFavorite.get();
        return getFavoritesMapper.favoritesMapper(favorite);
    }

}
