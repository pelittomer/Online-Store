package com.online_store.backend.api.variation.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;
import com.online_store.backend.api.variation.repository.VariationOptionRepository;
import com.online_store.backend.api.variation.repository.VariationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariationUtilsService {
    // services
    private final CategoryUtilsService categoryUtilsService;
    // repositories
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    public Category getCategoryById(Long categoryId) {
        return (categoryId != null) ? categoryUtilsService.findCategoryById(categoryId) : null;
    }

    public VariationOption findVariationOptionById(Long id) {
        return variationOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found!"));
    }

    public Variation findVariationById(Long id) {
        return variationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found!"));
    }
}