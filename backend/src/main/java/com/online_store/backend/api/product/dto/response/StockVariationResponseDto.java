package com.online_store.backend.api.product.dto.response;

import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockVariationResponseDto {
    private Long id;
    private String variation;

    private VariationOptionResponseDto variationOption;
}