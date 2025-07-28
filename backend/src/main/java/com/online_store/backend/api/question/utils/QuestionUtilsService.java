package com.online_store.backend.api.question.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.question.entities.Question;
import com.online_store.backend.api.question.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Question-related operations.
 * This component provides a helper method for retrieving question entities
 * with consistent error handling and logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QuestionUtilsService {
    // repositories
    private final QuestionRepository questionRepository;

    /**
     * Finds a question by its unique identifier.
     *
     * @param questionId The ID of the question to be retrieved.
     * @return The {@link Question} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no question with the given ID exists.
     * @see com.online_store.backend.api.question.service.QuestionService#answerQuestion(Long,
     *      com.online_store.backend.api.question.dto.request.AnswerRequestDto)
     */
    public Question findQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    log.warn("Question with ID {} not found.", questionId);
                    return new EntityNotFoundException("Question not found!");
                });
    }
}
