package com.online_store.backend.api.variation.dto.request;

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
public class StockVariationRequestDto {
    @NotNull(message = "Variation ID cannot be null.")
    @Min(value = 1, message = "Variation ID must be a positive number.")
    private Long variation;

    @NotNull(message = "Variation option ID cannot be null.")
    @Min(value = 1, message = "Variation option ID must be a positive number.")
    private Long variationOption;
}
