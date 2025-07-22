package com.online_store.backend.api.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.category.dto.request.CategoryRequestDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CommonUtilsService commonUtilsService;
    private final CategoryUtilsService categoryUtilsService;
    private final UploadService uploadService;

    @Transactional
    public String addCategory(CategoryRequestDto categoryRequestDto, MultipartFile imageFile, MultipartFile iconFile) {
        commonUtilsService.checkImageFileType(imageFile);
        commonUtilsService.checkImageFileType(iconFile);

        Upload imageUpload = uploadService.createFile(imageFile);
        Upload iconUpload = uploadService.createFile(iconFile);

        Category newCategory = Category.builder()
                .name(categoryRequestDto.getName())
                .description(categoryRequestDto.getDescription())
                .image(imageUpload)
                .icon(iconUpload)
                .build();

        if (categoryRequestDto.getParent() == null) {
            Long maxRight = categoryRepository.findMaxRightValue();
            if (maxRight == null) {
                newCategory.setLeftValue(1);
                newCategory.setRightValue(2);
            } else {
                newCategory.setLeftValue(maxRight.intValue() + 1);
                newCategory.setRightValue(maxRight.intValue() + 2);
            }
        } else {
            Category parentCategory = categoryRepository.findById(categoryRequestDto.getParent())
                    .orElseThrow(() -> new RuntimeException(
                            "Parent category not found with ID: " + categoryRequestDto.getParent()));

            categoryRepository.incrementRightValuesGreaterThanOrEqualTo(parentCategory.getRightValue(), 2);
            categoryRepository.incrementLeftValuesGreaterThan(parentCategory.getRightValue(), 2);

            newCategory.setLeftValue(parentCategory.getRightValue());
            newCategory.setRightValue(parentCategory.getRightValue() + 1);
            newCategory.setParent(parentCategory);
        }

        categoryRepository.save(newCategory);

        return "Category created successfully.";
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listLeafCategories() {
        return categoryRepository.findLeafCategories().stream()
                .map(categoryUtilsService::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(categoryUtilsService::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoryTree() {
        return categoryRepository.findAll().stream()
                .map(categoryUtilsService::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }
}
