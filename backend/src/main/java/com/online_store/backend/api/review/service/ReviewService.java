package com.online_store.backend.api.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.utils.ProductUtilsService;
import com.online_store.backend.api.review.dto.request.ReviewRequestDto;
import com.online_store.backend.api.review.dto.response.ReviewResponseDto;
import com.online_store.backend.api.review.entities.Review;
import com.online_store.backend.api.review.repository.ReviewRepository;
import com.online_store.backend.api.review.utils.mapper.CreateReviewMapper;
import com.online_store.backend.api.review.utils.mapper.GetReviewMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    //repositories
    private final ReviewRepository reviewRepository;
    //utils
    private final ProductUtilsService productUtilsService;
    private final CommonUtilsService commonUtilsService;
    //mappers
    private final CreateReviewMapper createReviewMapper;
    private final GetReviewMapper getReviewMapper;

    @Transactional
    public String createReview(ReviewRequestDto dto,
            List<MultipartFile> imageFiles) {
        User user = commonUtilsService.getCurrentUser();
        log.info("Attempting to create a review for product ID: {} by user: {}", dto.getProduct(), user.getEmail());

        Product product = productUtilsService.findProductById(dto.getProduct());

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            log.warn("User {} already has a review for product {}. Cannot create another one.", user.getEmail(),
                    product.getId());
            throw new Error("You are only one review");
        }

        Review review = createReviewMapper.reviewMapper(dto, product, imageFiles, user);
        reviewRepository.save(review);

        log.info("Review created successfully with ID: {} for product ID: {}", review.getId(), product.getId());
        return "Review created succesfully.";
    }

    public List<ReviewResponseDto> listProductReviews(Long productId) {
        log.info("Listing reviews for product with ID: {}", productId);

        Product product = productUtilsService.findProductById(productId);
        List<Review> reviews = reviewRepository.findByProduct(product);

        return reviews.stream()
                .map(getReviewMapper::reviewMapper).toList();
    }
}
