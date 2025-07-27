package com.online_store.backend.api.question.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.question.entities.Question;
import com.online_store.backend.api.question.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionUtilsService {
    // repositories
    private final QuestionRepository questionRepository;

    public Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    log.warn("Question with ID {} not found.", questionId);
                    return new EntityNotFoundException("Question not found!");
                });
    }
}
