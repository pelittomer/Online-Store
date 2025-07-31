package com.online_store.backend.api.variation.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.category.utils.CategoryUtilsService;
import com.online_store.backend.api.variation.dto.request.VariationRequestDto;
import com.online_store.backend.api.variation.dto.response.VariationResponseDto;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.repository.VariationRepository;
import com.online_store.backend.api.variation.utils.mapper.CreateVariationMapper;
import com.online_store.backend.api.variation.utils.mapper.GetVariationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing product variations.
 * This service handles the creation of new variations and the retrieval of
 * variations,
 * either globally or filtered by a specific category.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VariationService {
    // repositories
    private final VariationRepository variationRepository;
    // utils
    private final CategoryUtilsService categoryUtilsService;
    // mappers
    private final CreateVariationMapper createVariationMapper;
    private final GetVariationMapper getVariationMapper;

    /**
     * Adds a new product variation.
     * The variation can be associated with a specific category or be a global
     * variation.
     *
     * @param dto The DTO containing the variation's name and an optional category
     *            ID.
     * @return A success message upon successful creation.
     * @see com.online_store.backend.api.variation.controller.VariationController#addVariation(VariationRequestDto)
     */
    @Transactional
    public String addVariation(VariationRequestDto dto) {
        log.info("Adding new variation with name: '{}'", dto.getName());
        Category category = categoryUtilsService.findCategoryById(dto.getCategory());
        Variation variation = createVariationMapper.variationMapper(dto, category);
        variationRepository.save(variation);

        log.info("Variation created successfully with ID: {}", variation.getId());
        return "Variation created successfully.";
    }

    /**
     * Retrieves a list of variations.
     * This method can retrieve all global variations or all variations associated
     * with a specific category.
     * When a category ID is provided, it returns both global variations and those
     * specific to the category.
     *
     * @param categoryId An {@link Optional} containing the ID of the category to
     *                   filter by.
     * @return A list of {@link VariationResponseDto} objects.
     * @see com.online_store.backend.api.variation.controller.VariationController#listVariations(Optional)
     */
    @Transactional(readOnly = true)
    public List<VariationResponseDto> listVariations(Long categoryId) {
        log.info("Listing variations. Category ID is present: {}", categoryId);

        Category category = categoryUtilsService.findCategoryById(categoryId);

        return variationRepository.findByCategory(category).stream()
                .map(getVariationMapper::variationMapper)
                .collect(Collectors.toList());
    }

}