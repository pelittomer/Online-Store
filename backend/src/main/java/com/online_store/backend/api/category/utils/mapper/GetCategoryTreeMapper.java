package com.online_store.backend.api.category.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.dto.response.CategoryTreeDto;
import com.online_store.backend.api.category.entities.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCategoryTreeMapper {
    public CategoryTreeDto categoryMapper(Category dto) {
        return CategoryTreeDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage().getId())
                .icon(dto.getIcon().getId())
                .build();
    }
}
