package com.online_store.backend.api.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;
import com.online_store.backend.api.review.dto.request.ReviewRequestDto;
import com.online_store.backend.api.review.dto.response.ReviewResponseDto;
import com.online_store.backend.api.review.entities.Review;
import com.online_store.backend.api.review.repository.ReviewRepository;
import com.online_store.backend.api.review.utils.mapper.CreateReviewMapper;
import com.online_store.backend.api.review.utils.mapper.GetReviewMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CommonUtilsService commonUtilsService;
    private final CreateReviewMapper createReviewMapper;
    private final GetReviewMapper getReviewMapper;

    @Transactional
    public String createReview(ReviewRequestDto reviewRequestDto,
            List<MultipartFile> imageFiles) {
        User user = commonUtilsService.getCurrentUser();

        Product product = productRepository.findById(reviewRequestDto.getProduct())
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new Error("You are only one review");
        }

        Review review = createReviewMapper.reviewMapper(reviewRequestDto, product, imageFiles, user);

        reviewRepository.save(review);

        return "Review created succesfully.";
    }

    public List<ReviewResponseDto> listProductReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found!"));
        List<Review> reviews = reviewRepository.findByProduct(product);
        return reviews.stream()
                .map(getReviewMapper::reviewMapper).toList();
    }
}
