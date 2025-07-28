package com.online_store.backend.api.product.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
         * Processes dynamic multipart files from a request.
         * It iterates through all file names in the request and collects the
         * corresponding
         * non-empty files into a map.
         *
         * @param request The {@link MultipartHttpServletRequest} containing the files.
         * @return A map where the key is the file name and the value is a list of
         *         {@link MultipartFile}s.
         * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
         *      MultipartHttpServletRequest)
         */
        public Map<String, List<MultipartFile>> processDynamicFiles(MultipartHttpServletRequest request) {
                Map<String, List<MultipartFile>> dynamicFiles = new HashMap<>();
                Iterator<String> fileNames = request.getFileNames();

                while (fileNames.hasNext()) {
                        String fileName = fileNames.next();
                        List<MultipartFile> filesForThisKey = request.getFiles(fileName);

                        if (filesForThisKey != null && !filesForThisKey.isEmpty()) {
                                List<MultipartFile> actualFiles = filesForThisKey.stream()
                                                .filter(f -> !f.isEmpty())
                                                .toList();

                                if (!actualFiles.isEmpty()) {
                                        dynamicFiles.put(fileName, actualFiles);
                                }
                        }
                }
                return dynamicFiles;
        }

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