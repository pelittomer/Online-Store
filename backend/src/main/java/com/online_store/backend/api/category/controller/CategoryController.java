package com.online_store.backend.api.category.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public String addCategory(@RequestBody String categoryDetails) {
        // This function adds a new category.
        // 'categoryDetails' would contain the name, and potentially a parent category
        // ID.
        return "New category added: " + categoryDetails;
    }

    @GetMapping("/leafs")
    public String listLeafCategories() {
        // This function retrieves and lists all "leaf" categories.
        // Leaf categories are those that do not have any subcategories.
        return "List of leaf categories will be returned here.";
    }

    @GetMapping("/roots")
    public String listRootCategories() {
        // This function retrieves and lists all "root" categories.
        // Root categories are those that do not have a parent category.
        return "List of root categories will be returned here.";
    }

    @GetMapping("/tree")
    public String getCategoryTree() {
        // This function generates and returns the entire category tree structure.
        // This structure typically shows parent-child relationships between categories.
        return "The full category tree structure will be returned here.";
    }
}
