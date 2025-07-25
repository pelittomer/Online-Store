package com.online_store.backend.api.variation.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationOptionResponseDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VariationUtilsService {
    private final CategoryRepository categoryRepository;

    public Variation createVariationMapper(VariationRequestDto dto, Category category) {
        return Variation.builder()
                .name(dto.getName())
                .category(category)
                .build();
    }

    public VariationOption createVariationOptionMapper(String optionName, Variation variation) {
        return VariationOption.builder()
                .name(optionName)
                .variation(variation)
                .build();
    }

    public VariationResponseDto variationResponseMapper(Variation variation) {
        List<VariationOptionResponseDto> variationOptions = variation.getOptions().stream()
                .map(this::variationOptionResponseMapper)
                .collect(Collectors.toList());
        return VariationResponseDto.builder()
                .id(variation.getId())
                .name(variation.getName())
                .options(variationOptions)
                .build();
    }

    public VariationOptionResponseDto variationOptionResponseMapper(VariationOption variationOption) {
        return VariationOptionResponseDto.builder()
                .id(variationOption.getId())
                .name(variationOption.getName())
                .build();
    }

    public Category getCategoryById(Long categoryId) {
        return (categoryId != null) ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found!"))
                : null;
    }

}
