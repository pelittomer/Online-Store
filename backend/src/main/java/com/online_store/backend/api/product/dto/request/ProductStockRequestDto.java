package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequestDto {
    private Integer stockQuantity;
    private BigDecimal additionalPrice;
    private Boolean isLimited;
    private Integer replenishQuantity;
    @Valid
    @Builder.Default
    private List<StockVariationRequestDto> stockVariations = new ArrayList<>();
}