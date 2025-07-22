package com.online_store.backend.api.category.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.entities.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryUtilsService {

    public CategoryResponseDto mapCategoryToResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .image(category.getImage() != null ? category.getImage().getId() : null)
                .icon(category.getIcon() != null ? category.getIcon().getId() : null)
                .leftValue(category.getLeftValue())
                .rightValue(category.getRightValue())
                .parent(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }
}
