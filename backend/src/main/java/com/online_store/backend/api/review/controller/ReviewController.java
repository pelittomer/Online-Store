package com.online_store.backend.api.review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public String createReview(@RequestBody String reviewDetails) { // Likely a ReviewCreationDto
        // This function creates a new review.
        // 'reviewDetails' would typically include the product ID, rating, and comment.
        return "New review created: " + reviewDetails;
    }

    @GetMapping
    public String listProductReviews(@RequestParam String productId) { // Or @PathVariable if part of path
        // This function retrieves and lists all reviews for a specific product.
        // 'productId' is used to filter reviews relevant to that product.
        return "Reviews for product ID " + productId + " will be listed here.";
    }
}
