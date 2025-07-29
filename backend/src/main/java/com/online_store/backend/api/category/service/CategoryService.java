package com.online_store.backend.api.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.category.dto.request.CategoryRequestDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.dto.response.CategoryTreeDto;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.category.utils.mapper.CreateCategoryMapper;
import com.online_store.backend.api.category.utils.mapper.GetCategoryMapper;
import com.online_store.backend.api.category.utils.mapper.GetCategoryTreeMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.utils.CommonUtilsService;

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
    private final CommonUtilsService commonUtilsService;
    // mappers
    private final GetCategoryMapper getCategoryMapper;
    private final CreateCategoryMapper createCategoryMapper;
    private final GetCategoryTreeMapper getCategoryTreeMapper;
    // services
    private final UploadService uploadService;

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
        Upload imageUpload = handleFileUpload(imageFile);
        Upload iconUpload = handleFileUpload(iconFile);

        Category newCategory = createCategoryMapper.categoryMapper(dto, imageUpload, iconUpload);

        configureCategoryHierarchy(newCategory, dto.getParent());

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
    public List<CategoryTreeDto> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, CategoryTreeDto> categoryDTOMap = allCategories.stream()
                .map(getCategoryTreeMapper::categoryMapper)
                .collect(Collectors.toMap(CategoryTreeDto::getId, dto -> dto));

        List<CategoryTreeDto> rootCategories = new ArrayList<>();

        allCategories.forEach(category -> {
            CategoryTreeDto currentCategoryDto = categoryDTOMap.get(category.getId());

            if (category.getParent() != null) {
                CategoryTreeDto parentDTO = categoryDTOMap.get(category.getParent().getId());
                if (parentDTO != null) {
                    if (parentDTO.getChildren() == null) {
                        parentDTO.setChildren(new ArrayList<>());
                    }
                    parentDTO.getChildren().add(currentCategoryDto);
                }
            } else {
                rootCategories.add(currentCategoryDto);
            }
        });

        return rootCategories;
    }

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
    private void configureCategoryHierarchy(Category newCategory, Long parentId) {
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
        Category parentCategory = categoryUtilsService.findCategoryById(parentId);

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
    private Upload handleFileUpload(MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        return uploadService.createFile(file);
    }
}
