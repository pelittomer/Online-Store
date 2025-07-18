package com.online_store.backend.api.question.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.question.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public String createQuestion(@RequestBody String questionDetails) { // Likely a QuestionCreationDto
        // This function allows a user to create a new question, typically about a
        // product.
        // 'questionDetails' would include the question text and the associated product
        // ID.
        return "New question created: " + questionDetails;
    }

    @PostMapping("/{id}")
    public String answerQuestion(@PathVariable String id, @RequestBody String answerDetails) { // Likely an AnswerDto
        // This function enables a seller to provide an answer to a specific question
        // identified by its ID.
        // 'answerDetails' would contain the answer text.
        return "Answer for question ID " + id + " created: " + answerDetails;
    }

    @GetMapping
    public String listProductQuestions(@RequestParam String productId) { // Or @PathVariable if part of path
        // This function retrieves and lists all questions related to a specific
        // product.
        // 'productId' is used to filter questions relevant to that product.
        return "Questions for product ID " + productId + " will be listed here.";
    }

    @GetMapping("/all")
    public String listSellerQuestions() {
        // This function retrieves and lists all questions directed to or related to the
        // authenticated seller.
        // This might include questions about their products or general inquiries.
        return "All questions relevant to the seller will be listed here.";
    }
}
