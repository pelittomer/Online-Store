package com.online_store.backend.api.category.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

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
    // services
    private final UploadService uploadService;
    // utils
    private final CommonUtilsService commonUtilsService;
    // repositories
    private final CategoryRepository categoryRepository;

    /**
     * Configures the category hierarchy based on the parent ID.
     * This method uses the MPTT model to set the left and right values of the new
     * category.
     *
     * @param newCategory The new category entity to be configured.
     * @param parentId    The ID of the parent category, or {@code null} if it's a
     *                    root category.
     * @see com.online_store.backend.api.category.service.CategoryService#addCategory(com.online_store.backend.api.category.dto.request.CategoryRequestDto,
     *      MultipartFile, MultipartFile)
     */
    public void configureCategoryHierarchy(Category newCategory, Long parentId) {
        if (parentId == null) {
            handleRootCategory(newCategory);
        } else {
            handleChildCategory(newCategory, parentId);
        }
    }

    /**
     * Handles the logic for creating a new root category.
     * This method finds the maximum right value and assigns the left and right
     * values
     * to the new root category accordingly.
     *
     * @param newCategory The new root category to be handled.
     * @see com.online_store.backend.api.category.utils.CategoryUtilsService#configureCategoryHierarchy(Category,
     *      Long)
     */
    private void handleRootCategory(Category newCategory) {
        Long maxRight = categoryRepository.findMaxRightValue();
        int newLeft = (maxRight == null) ? 1 : maxRight.intValue() + 1;
        newCategory.setLeftValue(newLeft);
        newCategory.setRightValue(newLeft + 1);
        log.info("Setting '{}' as a new root category.", newCategory.getName());
    }

    /**
     * Handles the logic for creating a new child category.
     * This method shifts the left and right values of other categories to
     * accommodate the new child,
     * and sets the parent relationship.
     *
     * @param newCategory The new child category to be handled.
     * @param parentId    The ID of the parent category.
     * @see com.online_store.backend.api.category.utils.CategoryUtilsService#configureCategoryHierarchy(Category,
     *      Long)
     */
    private void handleChildCategory(Category newCategory, Long parentId) {
        Category parentCategory = findCategoryById(parentId);

        categoryRepository.incrementRightValuesGreaterThanOrEqualTo(parentCategory.getRightValue(), 2);
        categoryRepository.incrementLeftValuesGreaterThan(parentCategory.getRightValue(), 2);

        newCategory.setLeftValue(parentCategory.getRightValue());
        newCategory.setRightValue(parentCategory.getRightValue() + 1);
        newCategory.setParent(parentCategory);
        log.info("Setting '{}' as a child of category '{}'.", newCategory.getName(), parentCategory.getName());
    }

    /**
     * Handles the upload of an image file for a category.
     * It checks the file type and saves the file, returning the corresponding
     * {@link Upload} entity.
     *
     * @param file The image file to be uploaded.
     * @return The created {@link Upload} entity.
     * @see com.online_store.backend.api.category.service.CategoryService#addCategory(com.online_store.backend.api.category.dto.request.CategoryRequestDto,
     *      MultipartFile, MultipartFile)
     */
    public Upload handleFileUpload(MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        return uploadService.createFile(file);
    }

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
