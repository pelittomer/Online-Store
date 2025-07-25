package com.online_store.backend.api.review.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.review.dto.response.ReviewResponseDto;
import com.online_store.backend.api.review.entities.Review;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetReviewMapper {

    public ReviewResponseDto reviewMapper(Review dto) {
        List<Long> images = dto.getImages().stream()
                .map((item) -> item.getId()).toList();
        return ReviewResponseDto.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .rate(dto.getRate())
                .isPurchased(dto.getIsPurchased())
                .images(images)
                .product(dto.getProduct().getId())
                .build();
    }
}
