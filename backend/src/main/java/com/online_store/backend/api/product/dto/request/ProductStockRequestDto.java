package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.variation.dto.request.StockVariationRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
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
public class ProductStockRequestDto {
    @NotNull(message = "Stock quantity cannot be null.")
    @Min(value = 0, message = "Stock quantity cannot be negative.")
    private Integer stockQuantity;

    @NotNull(message = "Additional price cannot be null.")
    @DecimalMin(value = "0.0", message = "Additional price cannot be negative.")
    private BigDecimal additionalPrice;

    @NotNull(message = "isLimited status cannot be null.")
    private Boolean isLimited;

    @Min(value = 0, message = "Replenish quantity cannot be negative.")
    private Integer replenishQuantity;

    @Valid
    @Builder.Default
    private List<StockVariationRequestDto> stockVariations = new ArrayList<>();
}