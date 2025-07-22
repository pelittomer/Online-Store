package com.online_store.backend.api.brand.dto.request;

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
public class BrandRequestDto {
    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
    private String name;
}
