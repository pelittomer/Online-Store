package com.online_store.backend.api.brand.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

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
    // utils
    private final CommonUtilsService commonUtilsService;

    /**
     * Validates the data for creating a new brand.
     * It checks if a brand with the same name already exists and
     * validates the uploaded image file type.
     *
     * @param dto  The DTO containing the brand details.
     * @param file The image file for the brand.
     * @throws DuplicateResourceException if a brand with the same name already
     *                                    exists.
     * @see com.online_store.backend.api.brand.service.BrandService#addBrand(BrandRequestDto,
     *      MultipartFile)
     */
    public void validateBrandCreation(BrandRequestDto dto, MultipartFile file) {
        if (brandRepository.findByName(dto.getName()).isPresent()) {
            log.warn("Brand creation failed. Brand with name '{}' already exists.", dto.getName());
            throw new DuplicateResourceException("Brand with this name already exists.");
        }
        commonUtilsService.checkImageFileType(file);
    }

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
