package com.online_store.backend.api.brand.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Brand-related operations.
 * This component provides helper methods for validating brand data
 * and retrieving brand entities, ensuring consistent error handling and
 * logging.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BrandUtilsService {
    // repositories
    private final BrandRepository brandRepository;

    /**
     * Finds a brand by its ID.
     *
     * @param brandId The ID of the brand to find.
     * @return The {@link Brand} entity with the given ID.
     * @throws EntityNotFoundException if no brand with the specified ID is found.
     * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      org.springframework.web.multipart.MultipartHttpServletRequest)
     * 
     */
    public Brand findBrandById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> {
                    log.warn("Brand with ID {} not found.", brandId);
                    return new EntityNotFoundException("Brand not found!");
                });
    }
}
