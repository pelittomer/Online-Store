package com.online_store.backend.api.question.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDto {
    private Long id;
    private String question;
    private String answer;
    private Boolean isAnswered;
    private LocalDateTime questionDate;
    private LocalDateTime answerDate;
    private Long product;
}
