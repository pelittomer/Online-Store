package com.online_store.backend.api.product.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.product.entities.Product;
import com.online_store.backend.api.product.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Product-related operations.
 * This component provides helper methods for finding products and
 * processing dynamic multipart file uploads.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductUtilsService {
        // repositories
        private final ProductRepository productRepository;

        /**
         * Finds a product by its unique identifier.
         *
         * @param productId The ID of the product to be retrieved.
         * @return The {@link Product} entity corresponding to the provided ID.
         * @throws EntityNotFoundException if no product with the given ID exists.
         * @see com.online_store.backend.api.cart.service.CartService#addProductToCart(com.online_store.backend.api.cart.dto.request.CartRequestDto)
         * @see com.online_store.backend.api.favorite.service.FavoriteService#addProduct(com.online_store.backend.api.favorite.dto.request.FavoriteRequestDto)
         * @see com.online_store.backend.api.product.service.ProductService#getProductById(Long)
         * @see com.online_store.backend.api.question.service.QuestionService#createQuestion(com.online_store.backend.api.question.dto.request.QuestionRequestDto)
         * @see com.online_store.backend.api.question.service.QuestionService#listProductQuestions(Long)
         * @see com.online_store.backend.api.review.service.ReviewService#createReview(com.online_store.backend.api.review.dto.request.ReviewRequestDto,
         *      List)
         * @see com.online_store.backend.api.review.service.ReviewService#listProductReviews(Long)
         */
        public Product findProductById(Long productId) {
                return productRepository.findById(productId)
                                .orElseThrow(() -> {
                                        log.warn("Product with ID {} not found.", productId);
                                        return new EntityNotFoundException("Product not found!");
                                });
        }

}