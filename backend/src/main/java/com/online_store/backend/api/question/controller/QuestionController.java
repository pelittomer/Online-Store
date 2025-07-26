package com.online_store.backend.api.question.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.question.dto.request.AnswerRequestDto;
import com.online_store.backend.api.question.dto.request.QuestionRequestDto;
import com.online_store.backend.api.question.dto.response.QuestionResponseDto;
import com.online_store.backend.api.question.service.QuestionService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> createQuestion(@RequestBody QuestionRequestDto questionRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(questionService.createQuestion(questionRequestDto)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> answerQuestion(@PathVariable Long id,
            @RequestBody AnswerRequestDto answerRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(questionService.answerQuestion(id, answerRequestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> listProductQuestions(@RequestParam Long productId) {
        return ResponseEntity.ok(
                ApiResponse.success(questionService.listProductQuestions(productId)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> listSellerQuestions() {
        return ResponseEntity.ok(
                ApiResponse.success(questionService.listSellerQuestions()));
    }
}
