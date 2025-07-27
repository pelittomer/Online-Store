package com.online_store.backend.api.variation.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.utils.CategoryUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariationUtilsService {
    // services
    private final CategoryUtilsService categoryUtilsService;

    public Category getCategoryById(Long categoryId) {
        return (categoryId != null) ? categoryUtilsService.findCategoryById(categoryId) : null;
    }
}
