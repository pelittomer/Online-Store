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

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> addCategory(
            @Valid @RequestPart("category") CategoryRequestDto categoryRequestDto,
            @RequestPart(value = "image", required = true) MultipartFile imageFile,
            @RequestPart(value = "icon", required = true) MultipartFile iconFile) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        categoryService.addCategory(categoryRequestDto, imageFile, iconFile)));
    }

    @GetMapping("/leafs")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> listLeafCategories() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                    categoryService.listLeafCategories()));
    }

    @GetMapping("/roots")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> listRootCategories() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                    categoryService.listRootCategories()));
    }

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategoryTree() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                    categoryService.getCategoryTree()));
    }
}
