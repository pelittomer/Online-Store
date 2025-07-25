package com.online_store.backend.api.cart.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private Long id;
    @Builder.Default
    private List<CartItemResponseDto> cartItems = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
