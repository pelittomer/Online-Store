package com.online_store.backend.api.cart.dto.request;

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
    private Integer quantity=1;
    private Long product;
    private Long productStock;
}
