package com.online_store.backend.api.review.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
}
