package com.online_store.backend.api.question.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    
}
