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

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReview(
            @Valid @RequestPart("review") ReviewRequestDto reviewRequestDto,
            @RequestPart("images") List<MultipartFile> imageFiles) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        reviewService.createReview(reviewRequestDto, imageFiles)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> listProductReviews(@RequestParam Long productId) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        reviewService.listProductReviews(productId)));
    }
}
