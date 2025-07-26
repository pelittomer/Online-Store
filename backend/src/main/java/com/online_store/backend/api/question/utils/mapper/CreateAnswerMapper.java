package com.online_store.backend.api.question.utils.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.question.dto.request.AnswerRequestDto;
import com.online_store.backend.api.question.entities.Question;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateAnswerMapper {

    public Question answerMapper(Question question, AnswerRequestDto dto) {
        question.setAnswer(dto.getAnswer());
        question.setIsAnswered(true);
        question.setAnswerDate(LocalDateTime.now());
        return question;
    }
}
