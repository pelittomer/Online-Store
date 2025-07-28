package com.online_store.backend.api.brand.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.brand.repository.BrandRepository;
import com.online_store.backend.api.brand.utils.mapper.CreteBrandMapper;
import com.online_store.backend.api.brand.utils.mapper.GetBrandMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing brand-related operations.
 * Provides functionality for adding new brands and listing existing ones.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
    // repositories
    private final BrandRepository brandRepository;
    // services
    private final UploadService uploadService;
    // utils
    private final CommonUtilsService commonUtilsService;
    // mappers
    private final CreteBrandMapper createBrandMapper;
    private final GetBrandMapper getBrandMapper;

    /**
     * Adds a new brand to the system.
     * This method validates the brand data, uploads the brand logo, and saves the
     * new brand.
     *
     * @param dto  The DTO containing the brand details.
     * @param file The logo image file for the brand.
     * @return A success message upon successful brand creation.
     * @see com.online_store.backend.api.brand.controller.BrandController#addBrand(BrandRequestDto,
     *      MultipartFile)
     */
    @Transactional
    public String addBrand(BrandRequestDto dto, MultipartFile file) {
        log.info("Attempting to add a new brand with name: '{}'", dto.getName());
        validateBrandCreation(dto, file);

        Upload logo = uploadService.createFile(file);
        Brand newBrand = createBrandMapper.brandMapper(dto, logo);
        brandRepository.save(newBrand);

        log.info("Brand '{}' created successfully with ID: {}.", newBrand.getName(), newBrand.getId());
        return "Brand created successfuly.";
    }

    /**
     * Retrieves a list of all brands.
     *
     * @return A list of {@link BrandResponseDto} objects representing all brands.
     * @see com.online_store.backend.api.brand.controller.BrandController#listBrands()
     */
    @Transactional(readOnly = true)
    public List<BrandResponseDto> listBrands() {
        return brandRepository.findAll().stream()
                .map(getBrandMapper::brandMapper)
                .collect(Collectors.toList());
    }

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
    private void validateBrandCreation(BrandRequestDto dto, MultipartFile file) {
        if (brandRepository.findByName(dto.getName()).isPresent()) {
            log.warn("Brand creation failed. Brand with name '{}' already exists.", dto.getName());
            throw new DuplicateResourceException("Brand with this name already exists.");
        }
        commonUtilsService.checkImageFileType(file);
    }
}
