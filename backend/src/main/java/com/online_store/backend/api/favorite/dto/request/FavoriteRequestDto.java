package com.online_store.backend.api.favorite.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequestDto {
    @NotNull(message = "Product ID cannot be null.")
    @Min(value = 1, message = "Product ID must be a positive number.")
    private Long product;
}
