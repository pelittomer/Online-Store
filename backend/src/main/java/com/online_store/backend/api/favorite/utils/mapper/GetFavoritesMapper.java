package com.online_store.backend.api.favorite.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.favorite.dto.response.FavoriteResponseDto;
import com.online_store.backend.api.favorite.entities.Favorite;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.utils.mapper.GetProductMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetFavoritesMapper {
    private final GetProductMapper getProductMapper;

    public FavoriteResponseDto favoritesMapper(Favorite dto) {
        List<ProductResponseDto> products = dto.getProducts().stream()
                .map(getProductMapper::prouctMapper).toList();

        return FavoriteResponseDto.builder()
                .id(dto.getId())
                .products(products)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
