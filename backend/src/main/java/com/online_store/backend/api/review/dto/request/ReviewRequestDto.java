package com.online_store.backend.api.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    @NotBlank(message = "Comment cannot be blank.")
    @Size(min = 10, max = 1000, message = "Comment must be between 10 and 1000 characters.")
    private String comment;

    @NotNull(message = "Rate cannot be null.")
    @Min(value = 1, message = "Rate must be at least 1.")
    @Max(value = 5, message = "Rate cannot be more than 5.")
    private Integer rate;

    @NotNull(message = "Product ID cannot be null.")
    @Min(value = 1, message = "Product ID must be a positive number.")
    private Long product;
}
