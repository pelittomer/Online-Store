package com.online_store.backend.api.upload.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.repository.UploadRepository;
import com.online_store.backend.api.upload.utils.UploadUtilsService;
import com.online_store.backend.api.upload.utils.mapper.CreateUploadMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for handling file uploads.
 * This service provides methods for creating, updating, and retrieving files
 * that are stored in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {
    // repositories
    private final UploadRepository uploadRepository;
    // mappers
    private final CreateUploadMapper createUploadMapper;
    // utils
    private final UploadUtilsService uploadUtilsService;

    /**
     * Creates a new upload record from a multipart file.
     * The file's content is read and stored in the database.
     *
     * @param file The {@link MultipartFile} to be uploaded.
     * @return The newly created {@link Upload} entity.
     * @throws RuntimeException if there's an error reading the file content.
     * @see com.online_store.backend.api.brand.service.BrandService#addBrand(com.online_store.backend.api.brand.dto.request.BrandRequestDto,
     *      MultipartFile)
     * @see com.online_store.backend.api.category.utils.CategoryUtilsService#handleFileUpload(MultipartFile)
     * @see com.online_store.backend.api.company.service.CompanyService#createCompany(com.online_store.backend.api.company.dto.request.CompanyRequestDto,
     *      MultipartFile)
     * @see com.online_store.backend.api.product.utils.mapper.CreateProductMapper#productMapper(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      java.util.Map, com.online_store.backend.api.company.entities.Company,
     *      com.online_store.backend.api.shipper.entities.Shipper,
     *      com.online_store.backend.api.brand.entities.Brand,
     *      com.online_store.backend.api.category.entities.Category)
     * @see com.online_store.backend.api.profile.utils.ProfileUtilsService#updateAvatarIfPresent(com.online_store.backend.api.profile.entities.Profile,
     *      MultipartFile)
     * @see com.online_store.backend.api.review.utils.mapper.CreateReviewMapper#reviewMapper(com.online_store.backend.api.review.dto.request.ReviewRequestDto,
     *      com.online_store.backend.api.product.entities.Product, java.util.List,
     *      com.online_store.backend.api.user.entities.User)
     * @see com.online_store.backend.api.shipper.service.ShipperService#createShipper(com.online_store.backend.api.shipper.dto.request.ShipperRequestDto,
     *      MultipartFile)
     */
    @Transactional
    public Upload createFile(MultipartFile file) {
        log.info("Creating a new upload record for file: {}", file.getOriginalFilename());
        try {
            Upload upload = createUploadMapper.uploadMapper(file);
            return uploadRepository.save(upload);
        } catch (IOException e) {
            log.error("Failed to read file content for upload: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    /**
     * Updates the content of an existing upload record.
     * This method reads the content of a new file and replaces the content of an
     * existing upload record.
     *
     * @param currentFile The existing {@link Upload} entity to be updated.
     * @param file        The new {@link MultipartFile} containing the updated
     *                    content.
     * @return The updated {@link Upload} entity.
     * @throws RuntimeException        if there's an error reading the file content.
     * @throws EntityNotFoundException if the existing upload record is not found.
     * @see com.online_store.backend.api.company.utils.CompanyUtilsService#updateCompanyLogoIfPresent(com.online_store.backend.api.company.entities.Company,
     *      MultipartFile)
     * @see com.online_store.backend.api.profile.utils.ProfileUtilsService#updateAvatarIfPresent(com.online_store.backend.api.profile.entities.Profile,
     *      MultipartFile)
     */
    @Transactional
    public Upload updateExistingUploadContent(Upload currentFile, MultipartFile file) {
        log.info("Updating content for existing upload record with ID: {}", currentFile.getId());
        try {
            Upload uploadItem = uploadRepository.findById(currentFile.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Upload not found!"));

            uploadItem.setFileContent(file.getBytes());
            return uploadRepository.save(uploadItem);
        } catch (Exception e) {
            log.error("Failed to read file content for update: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Could not store file " + file.getOriginalFilename());
        }
    }

    /**
     * Retrieves an upload record by its ID.
     *
     * @param id The ID of the upload record to retrieve.
     * @return The {@link Upload} entity.
     * @throws EntityNotFoundException if the upload record is not found.
     * @see com.online_store.backend.api.upload.controller.UploadController#getUploadById(Long)
     */
    @Transactional(readOnly = true)
    public Upload getUploadById(Long id) {
        log.debug("Fetching upload record with ID: {}", id);
        return uploadUtilsService.findUploadById(id);
    }

}
