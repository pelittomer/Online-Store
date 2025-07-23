package com.online_store.backend.api.variation.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.repository.CategoryRepository;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;
import com.online_store.backend.api.variation.repository.VariationRepository;
import com.online_store.backend.api.variation.utils.VariationUtilsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariationService {
    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;
    private final VariationUtilsService variationUtilsService;

    @Transactional
    public String addVariation(VariationRequestDto variationRequestDto) {
        Category category = variationUtilsService.getCategoryById(variationRequestDto.getCategory());

        Variation variation = variationUtilsService.createVariationMapper(variationRequestDto, category);

        Set<VariationOption> variationOptions = variationRequestDto.getOptions().stream()
                .map(optionName -> variationUtilsService.createVariationOptionMapper(optionName, variation))
                .collect(Collectors.toSet());

        variation.setOptions(variationOptions);
        variationRepository.save(variation);

        return "Variation created successfully.";
    }

    @Transactional(readOnly = true)
    public List<VariationResponseDto> listVariations(Optional<Long> categoryId) {
        Stream<Variation> variationsStream = variationRepository.findByCategoryIsNull().stream();

        if (categoryId.isPresent()) {
            Long id = categoryId.get();
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isPresent()) {
                variationsStream = Stream.concat(
                        variationsStream,
                        variationRepository.findByCategory(categoryOptional.get()).stream());
            }
        }
        return variationsStream
                .map(variationUtilsService::variationResponseMapper)
                .collect(Collectors.toList());
    }

}
