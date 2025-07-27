package com.online_store.backend.api.question.dto.request;

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
public class QuestionRequestDto {
    @NotBlank(message = "Question cannot be blank.")
    @Size(min = 10, max = 255, message = "Question must be between 10 and 255 characters.")
    private String question;

    @NotNull(message = "Product ID cannot be null.")
    @Min(value = 1, message = "Product ID must be a positive number.")
    private Long product;
}
