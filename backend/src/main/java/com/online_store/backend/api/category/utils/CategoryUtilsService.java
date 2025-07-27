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

    public void configureCategoryHierarchy(Category newCategory, Long parentId) {
        if (parentId == null) {
            handleRootCategory(newCategory);
        } else {
            handleChildCategory(newCategory, parentId);
        }
    }

    private void handleRootCategory(Category newCategory) {
        Long maxRight = categoryRepository.findMaxRightValue();
        int newLeft = (maxRight == null) ? 1 : maxRight.intValue() + 1;
        newCategory.setLeftValue(newLeft);
        newCategory.setRightValue(newLeft + 1);
        log.info("Setting '{}' as a new root category.", newCategory.getName());
    }

    private void handleChildCategory(Category newCategory, Long parentId) {
        Category parentCategory = findCategoryById(parentId);

        categoryRepository.incrementRightValuesGreaterThanOrEqualTo(parentCategory.getRightValue(), 2);
        categoryRepository.incrementLeftValuesGreaterThan(parentCategory.getRightValue(), 2);

        newCategory.setLeftValue(parentCategory.getRightValue());
        newCategory.setRightValue(parentCategory.getRightValue() + 1);
        newCategory.setParent(parentCategory);
        log.info("Setting '{}' as a child of category '{}'.", newCategory.getName(), parentCategory.getName());
    }

    public Upload handleFileUpload(MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        return uploadService.createFile(file);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Parent category not found with ID: {}", id);
                    return new RuntimeException("Parent category not found with ID: " + id);
                });
    }
}
