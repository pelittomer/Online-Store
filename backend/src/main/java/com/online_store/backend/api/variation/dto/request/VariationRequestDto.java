package com.online_store.backend.api.variation.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariationRequestDto {

    @NotBlank(message = "Variation name cannot be empty.")
    @Size(min = 2, max = 100, message = "Variation name must be between 2 and 100 characters.")
    private String name;

    private Long category;

    @NotEmpty(message = "Variation options cannot be empty.")
    @Size(min = 1, message = "At least one option must be provided for the variation.")
    private List<@NotBlank(message = "Variation option name cannot be empty.") @Size(min = 1, max = 100, message = "Variation option name must be between 1 and 100 characters.") String> options;
}
