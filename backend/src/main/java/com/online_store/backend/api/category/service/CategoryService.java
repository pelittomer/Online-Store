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

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    //repositories
    private final CategoryRepository categoryRepository;
    //utils
    private final CategoryUtilsService categoryUtilsService;
    //mappers
    private final GetCategoryMapper getCategoryMapper;
    private final CreateCategoryMapper createCategoryMapper;

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

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listLeafCategories() {
        return categoryRepository.findLeafCategories().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> listRootCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getCategoryTree() {
        return categoryRepository.findAll().stream()
                .map(getCategoryMapper::mapCategoryToResponseDto)
                .collect(Collectors.toList());
    }
}
