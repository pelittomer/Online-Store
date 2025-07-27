package com.online_store.backend.api.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.category.dto.request.CategoryRequestDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.service.CategoryService;
import com.online_store.backend.common.exception.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing product categories.
 * Provides endpoints to add a new category and to retrieve category information
 * in various formats.
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Endpoint to add a new category.
     * This method accepts category details, an image file, and an icon file.
     *
     * @param categoryRequestDto The category details, including its name and parent
     *                           ID.
     * @param imageFile          The image file for the category.
     * @param iconFile           The icon file for the category.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> addCategory(
            @Valid @RequestPart("category") CategoryRequestDto categoryRequestDto,
            @RequestPart(value = "image", required = true) MultipartFile imageFile,
            @RequestPart(value = "icon", required = true) MultipartFile iconFile) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        categoryService.addCategory(categoryRequestDto, imageFile, iconFile)));
    }

    /**
     * Endpoint to get a list of all leaf categories (categories without children).
     *
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         list of {@link CategoryResponseDto}.
     */
    @GetMapping("/leafs")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> listLeafCategories() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        categoryService.listLeafCategories()));
    }

    /**
     * Endpoint to get a list of all root categories (top-level categories without
     * parents).
     *
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         list of {@link CategoryResponseDto}.
     */
    @GetMapping("/roots")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> listRootCategories() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        categoryService.listRootCategories()));
    }

    /**
     * Endpoint to get a flat list of all categories, representing the entire
     * category tree.
     *
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         list of all {@link CategoryResponseDto}.
     */
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategoryTree() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        categoryService.getCategoryTree()));
    }
}
