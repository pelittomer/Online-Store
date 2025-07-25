package com.online_store.backend.api.product.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.product.dto.base.FeatureDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDetailResponseDto {
    private Long id;
    private String description;
    private String shortDescription;
    @Builder.Default
    private List<FeatureDto> features = new ArrayList<>();
    @Builder.Default
    private List<ProductCriteriaResponseDto> productCriterias = new ArrayList<>();
}
