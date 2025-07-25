package com.online_store.backend.api.product.dto.response;

import java.math.BigDecimal;
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
public class ProductStockResponseDto {
    private Long id;
    private Integer stockQuantity;
    private BigDecimal additionalPrice;
    private Boolean isLimited;
    private Integer replenishQuantity;
    @Builder.Default
    private List<StockVariationResponseDto> stockVariations = new ArrayList<>();
}
