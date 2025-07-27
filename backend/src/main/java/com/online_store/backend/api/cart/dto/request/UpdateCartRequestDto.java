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
public class UpdateCartRequestDto {
    @NotNull(message = "Cart ID cannot be null.")
    @Min(value = 1, message = "Cart ID must be a positive number.")
    private Long id;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;
}
