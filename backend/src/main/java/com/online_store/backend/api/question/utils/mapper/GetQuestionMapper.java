package com.online_store.backend.api.question.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.question.dto.response.QuestionResponseDto;
import com.online_store.backend.api.question.entities.Question;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetQuestionMapper {

    public QuestionResponseDto questionMapper(Question dto) {
        return QuestionResponseDto.builder()
                .id(dto.getId())
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .isAnswered(dto.getIsAnswered())
                .questionDate(dto.getQuestionDate())
                .answerDate(dto.getAnswerDate())
                .product(dto.getProduct().getId())
                .build();
    }
}
