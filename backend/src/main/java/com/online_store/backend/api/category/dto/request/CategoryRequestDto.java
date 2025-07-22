package com.online_store.backend.api.category.dto.request;

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
public class CategoryRequestDto {
    @NotBlank(message = "Category name cannot be empty.")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters.")
    private String name;

    @NotBlank(message = "Category description cannot be empty.")
    @Size(min = 10, max = 1000, message = "Category description must be between 10 and 1000 characters.")
    private String description;

    private Long parent;
}
