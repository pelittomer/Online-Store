package com.online_store.backend.api.question.dto.request;

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
public class AnswerRequestDto {
    @NotBlank(message = "Answer cannot be blank.")
    @Size(min = 10, max = 500, message = "Answer must be between 10 and 500 characters.")
    private String answer;
}
