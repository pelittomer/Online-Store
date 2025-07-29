package com.online_store.backend.api.variation.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.category.entities.Category;
import com.online_store.backend.api.variation.entities.Variation;
import com.online_store.backend.api.variation.entities.VariationOption;
import com.online_store.backend.api.variation.repository.VariationOptionRepository;
import com.online_store.backend.api.variation.repository.VariationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Variation-related operations.
 * This component provides helper methods for retrieving variation and variation
 * option entities
 * with consistent error handling.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VariationUtilsService {
    // repositories
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    /**
     * Finds a variation option by its unique identifier.
     *
     * @param id The ID of the variation option to be retrieved.
     * @return The {@link VariationOption} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no variation option with the given ID
     *                                 exists.
     * @see com.online_store.backend.api.product.utils.mapper.CreateProductMapper#productMapper(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      java.util.Map, com.online_store.backend.api.company.entities.Company,
     *      com.online_store.backend.api.shipper.entities.Shipper,
     *      com.online_store.backend.api.brand.entities.Brand, Category)
     */
    public VariationOption findVariationOptionById(Long id) {
        return variationOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found!"));
    }

    /**
     * Finds a variation by its unique identifier.
     *
     * @param id The ID of the variation to be retrieved.
     * @return The {@link Variation} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no variation with the given ID exists.
     * @see com.online_store.backend.api.product.utils.mapper.CreateProductMapper#productMapper(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      java.util.Map, com.online_store.backend.api.company.entities.Company,
     *      com.online_store.backend.api.shipper.entities.Shipper,
     *      com.online_store.backend.api.brand.entities.Brand, Category)
     */
    public Variation findVariationById(Long id) {
        return variationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found!"));
    }
}