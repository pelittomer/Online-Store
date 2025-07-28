package com.online_store.backend.api.category.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Category-related operations.
 * This component provides helper methods for managing category hierarchy using
 * the MPTT (Modified Preorder Tree Traversal) model, handling file uploads,
 * and retrieving category entities.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryUtilsService {
    // repositories
    private final CategoryRepository categoryRepository;

    /**
     * Finds a category by its ID.
     *
     * @param id The ID of the category to find.
     * @return The {@link Category} entity with the given ID.
     * @throws RuntimeException if the category is not found.
     * @see com.online_store.backend.api.category.utils.CategoryUtilsService#handleChildCategory(Category,
     *      Long)
     * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      org.springframework.web.multipart.MultipartHttpServletRequest)
     * @see com.online_store.backend.api.variation.utils.VariationUtilsService#getCategoryById(Long)
     */
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Parent category not found with ID: {}", id);
                    return new RuntimeException("Parent category not found with ID: " + id);
                });
    }
}
