package com.online_store.backend.api.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockVariationRequestDto {
    private Long variation;
    private Long variationOption;
}
