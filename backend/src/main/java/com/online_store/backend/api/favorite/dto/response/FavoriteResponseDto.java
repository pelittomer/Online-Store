package com.online_store.backend.api.favorite.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.product.dto.response.ProductResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponseDto {
    private Long id;
    @Builder.Default
    private List<ProductResponseDto> products = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
