package com.online_store.backend.api.order.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.order.entities.OrderStatus;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.dto.response.StockVariationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private OrderStatus status;
    private ProductResponseDto product;
    @Builder.Default
    private List<StockVariationResponseDto> stockVariation = new ArrayList<>();
}
