package com.online_store.backend.api.product.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.product.dto.base.FeatureDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequestDto {
    @Size(max = 2000, message = "Description cannot exceed 2000 characters.")
    private String description;

    @Size(max = 500, message = "Short description cannot exceed 500 characters.")
    private String shortDescription;

    @Valid
    @Builder.Default
    private List<FeatureDto> features = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<ProductCriteriaRequestDto> productCriterias = new ArrayList<>();
}
