package com.online_store.backend.api.cart.dto.request;

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
public class CartRequestDto {
    @Builder.Default
    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity = 1;

    @NotNull(message = "Product ID cannot be null.")
    @Min(value = 1, message = "Product ID must be a positive number.")
    private Long product;

    @NotNull(message = "Product Stock ID cannot be null.")
    @Min(value = 1, message = "Product Stock ID must be a positive number.")
    private Long productStock;
}
