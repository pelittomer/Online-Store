package com.online_store.backend.api.category.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.dto.request.CategoryRequestDto;
import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateCategoryMapper {

    public Category categoryMapper(CategoryRequestDto dto,
            Upload imageUpload,
            Upload iconUpload) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .image(imageUpload)
                .icon(iconUpload)
                .build();
    }
}
