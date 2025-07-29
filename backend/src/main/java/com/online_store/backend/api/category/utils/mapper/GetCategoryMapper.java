package com.online_store.backend.api.category.utils.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.category.entities.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCategoryMapper {

    public CategoryResponseDto mapCategoryToResponseDto(Category dto) {
        Long parentId = (dto.getParent() != null) ? dto.getParent().getId() : null;

        return CategoryResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage().getId())
                .icon(dto.getIcon().getId())
                .leftValue(dto.getLeftValue())
                .rightValue(dto.getRightValue())
                .parent(parentId)
                .build();
    }
}
