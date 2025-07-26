package com.online_store.backend.api.question.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.question.dto.request.QuestionRequestDto;
import com.online_store.backend.api.question.entities.Question;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateQuestionMapper {

    public Question questionMapper(QuestionRequestDto dto,
            Product product,
            User user) {
        return Question.builder()
                .question(dto.getQuestion())
                .product(product)
                .user(user)
                .company(product.getCompany())
                .build();
    }
}
