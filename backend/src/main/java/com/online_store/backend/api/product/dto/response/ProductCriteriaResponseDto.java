package com.online_store.backend.api.product.dto.response;

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
public class ProductCriteriaResponseDto {
    private Long id;
    private ProductVariationDto variation;
    @Builder.Default
    private List<CriteriaOptionResponseDto> criteriaOptions = new ArrayList<>();
}
