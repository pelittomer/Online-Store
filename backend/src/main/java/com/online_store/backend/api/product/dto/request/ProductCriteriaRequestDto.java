package com.online_store.backend.api.product.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
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
public class ProductCriteriaRequestDto {
    @NotNull(message = "Variation ID cannot be null.")
    @Min(value = 1, message = "Variation ID must be a positive number.")
    private Long variation;
    
    @Valid
    @Builder.Default
    private List<CriteriaOptionRequestDto> criteriaOptions = new ArrayList<>();
}
