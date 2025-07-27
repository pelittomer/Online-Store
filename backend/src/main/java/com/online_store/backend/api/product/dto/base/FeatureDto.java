package com.online_store.backend.api.product.dto.base;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDto {
    @NotBlank(message = "Feature name cannot be blank.")
    @Size(max = 100, message = "Feature name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Feature value cannot be blank.")
    @Size(max = 255, message = "Feature value cannot exceed 255 characters.")
    private String value;
}