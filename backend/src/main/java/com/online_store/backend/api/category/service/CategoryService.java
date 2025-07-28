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
import com.online_store.backend.api.category.utils.mapper.CreateCategoryMapper;
import com.online_store.backend.api.category.utils.mapper.GetCategoryMapper;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing product categories.
 * This service handles the creation of new categories and provides
 * various methods for retrieving category lists, including
 * root categories, leaf categories, and the full category tree.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    // repositories
    private final CategoryRepository categoryRepository;
    // utils
    private final CategoryUtilsService categoryUtilsService;
    // mappers
    private final GetCategoryMapper getCategoryMapper;
    private final CreateCategoryMapper createCategoryMapper;

    /**
     * Adds a new category to the system.
     * This method handles file uploads for the category's image and icon,
     * configures its hierarchical position using the MPTT model, and saves it to
     * the database.
     *
     * @param dto       The DTO containing category details like name and parent ID.
     * @param imageFile The image file for the category.
     * @param iconFile  The icon file for the category.
     * @return A success message indicating the category was created.
     * @see com.online_store.backend.api.category.controller.CategoryController#addCategory(CategoryRequestDto,
     *      MultipartFile, MultipartFile)
     */
    @Transactional
    public String addCategory(CategoryRequestDto dto, MultipartFile imageFile, MultipartFile iconFile) {
        Upload imageUpload = categoryUtilsService.handleFileUpload(imageFile);
        Upload iconUpload = categoryUtilsService.handleFileUpload(iconFile);

        Category newCategory = createCategoryMapper.categoryMapper(dto, imageUpload, iconUpload);

        categoryUtilsService.configureCategoryHierarchy(newCategory, dto.getParent());

        categoryRepository.save(newCategory);

        log.info("Category '{}' created successfully.", newCategory.getName());

        return "Category created successfully.";
    }

    /**
     * Retrieves all categories that do not have any child categories (leaf nodes).
     *
     * @return A list of {@link CategoryResponseDto} representing the leaf
     *         categories.
     * @see com.online_store.backend.api.category.controller.CategoryController#listLeafCategories()
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listLeafCategories() {
        return categoryRepository.findLeafCategories().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all top-level categories that do not have a parent (root nodes).
     *
     * @return A list of {@link CategoryResponseDto} representing the root
     *         categories.
     * @see com.online_store.backend.api.category.controller.CategoryController#listRootCategories()
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all categories, effectively providing a flat list of the entire
     * category tree.
     *
     * @return A list of all {@link CategoryResponseDto}.
     * @see com.online_store.backend.api.category.controller.CategoryController#getCategoryTree()
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoryTree() {
        return categoryRepository.findAll().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }
}
