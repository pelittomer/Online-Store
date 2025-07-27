package com.online_store.backend.api.review.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.review.dto.request.ReviewRequestDto;
import com.online_store.backend.api.review.dto.response.ReviewResponseDto;
import com.online_store.backend.api.review.service.ReviewService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing product reviews.
 * This controller provides endpoints for users to create a new review for a
 * product
 * and for retrieving all reviews for a specific product.
 */
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
        private final ReviewService reviewService;

        /**
         * Endpoint to create a new review for a product.
         * This endpoint accepts review details and an optional list of image files.
         *
         * @param reviewRequestDto The DTO containing the review's content, rating, and
         *                         product ID.
         * @param imageFiles       A list of {@link MultipartFile}s for images
         *                         associated with the review.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> createReview(
                        @Valid @RequestPart("review") ReviewRequestDto reviewRequestDto,
                        @RequestPart("images") List<MultipartFile> imageFiles) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                reviewService.createReview(reviewRequestDto, imageFiles)));
        }

        /**
         * Endpoint to get a list of all reviews for a specified product.
         *
         * @param productId The ID of the product for which to retrieve reviews.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link ReviewResponseDto}.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> listProductReviews(@RequestParam Long productId) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                reviewService.listProductReviews(productId)));
        }
}
