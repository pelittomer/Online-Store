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

/**
 * REST controller for managing questions and answers on products.
 * This controller provides endpoints for users to ask questions, for sellers to
 * answer them,
 * and to retrieve lists of questions for a specific product or seller.
 */
@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
        private final QuestionService questionService;

        /**
         * Endpoint for a customer to create a new question about a product.
         * The question is linked to the product and the user.
         *
         * @param questionRequestDto The DTO containing the product ID and the question
         *                           text.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping()
        public ResponseEntity<ApiResponse<String>> createQuestion(@RequestBody QuestionRequestDto questionRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                questionService.createQuestion(questionRequestDto)));
        }

        /**
         * Endpoint for a seller to answer a specific question.
         * The seller can only answer questions related to products from their company.
         *
         * @param id               The ID of the question to be answered.
         * @param answerRequestDto The DTO containing the answer text.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> answerQuestion(@PathVariable Long id,
                        @RequestBody AnswerRequestDto answerRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                questionService.answerQuestion(id, answerRequestDto)));
        }

        /**
         * Endpoint to retrieve a list of all questions for a given product.
         *
         * @param productId The ID of the product.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link QuestionResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> listProductQuestions(
                        @RequestParam Long productId) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                questionService.listProductQuestions(productId)));
        }

        /**
         * Endpoint for a seller to retrieve a list of all questions directed to their
         * company's products.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link QuestionResponseDto}.
         */
        @GetMapping("/all")
        public ResponseEntity<ApiResponse<List<QuestionResponseDto>>> listSellerQuestions() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                questionService.listSellerQuestions()));
        }
}
