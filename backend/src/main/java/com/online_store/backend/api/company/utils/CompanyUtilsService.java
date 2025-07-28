package com.online_store.backend.api.company.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.repository.CompanyRepository;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Company-related operations.
 * Provides helper methods for retrieving company entities, validating company
 * data
 * during creation and updates, and managing company logos.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyUtilsService {
    // utils
    private final CommonUtilsService commonUtilsService;
    // repositories
    private final CompanyRepository companyRepository;
    // services
    private final UploadService uploadService;

    /**
     * Retrieves the company associated with the currently authenticated user.
     *
     * @return The {@link Company} entity of the current user.
     * @throws EntityNotFoundException if no company is found for the current user.
     * @see com.online_store.backend.api.product.service.ProductService#addProduct(com.online_store.backend.api.product.dto.request.ProductRequestDto,
     *      org.springframework.web.multipart.MultipartHttpServletRequest)
     * @see com.online_store.backend.api.question.service.QuestionService#answerQuestion(Long,
     *      com.online_store.backend.api.question.dto.request.AnswerRequestDto)
     * @see com.online_store.backend.api.question.service.QuestionService#listSellerQuestions()
     * @see com.online_store.backend.api.returnRequest.service.ReturnRequestService#updateReturnRequestStatus(Long,
     *      com.online_store.backend.api.returnRequest.dto.request.UpdateReturnRequestDto)
     */
    public Company getCurrentUserCompany() {
        User currentUser = commonUtilsService.getCurrentUser();
        Company company = companyRepository.findByUser(currentUser)
                .orElseThrow(() -> {
                    log.warn("Company not found for current user: {}", currentUser.getEmail());
                    return new EntityNotFoundException("Company not found!");
                });
        return company;
    }

    /**
     * Finds a company by its ID.
     *
     * @param id The ID of the company to find.
     * @return The {@link Company} entity with the given ID.
     * @throws EntityNotFoundException if no company with the specified ID is found.
     * @see com.online_store.backend.api.company.service.CompanyService#updateCompanyStatus(Long,
     *      com.online_store.backend.api.company.dto.request.CompanyUpdateStatusRequestDto)
     * @see com.online_store.backend.api.company.service.CompanyService#getCompanyById(Long)
     */
    public Company findCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found with ID: {}", id);
                    return new EntityNotFoundException("Company with ID " + id + " not found.");
                });
    }

    /**
     * Finds a company associated with a specific user.
     *
     * @param user The user entity whose company is to be found.
     * @return The {@link Company} entity of the specified user.
     * @throws EntityNotFoundException if no company is found for the given user.
     * @see com.online_store.backend.api.company.service.CompanyService#updateMyCompany(com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto,
     *      MultipartFile)
     * @see com.online_store.backend.api.company.service.CompanyService#getMyCompany()
     */
    public Company findCompanyByUser(User user) {
        return companyRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.warn("Company not found for user: {}", user.getEmail());
                    return new EntityNotFoundException("Company not found for the current user.");
                });
    }

    /**
     * Updates a company's logo if a new file is provided.
     * This method checks if the file is valid and then updates the associated
     * {@link com.online_store.backend.api.upload.entities.Upload} entity.
     *
     * @param company The company entity to update.
     * @param file    The new logo file, or {@code null} if no update is needed.
     * @see com.online_store.backend.api.company.service.CompanyService#updateMyCompany(com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto,
     *      MultipartFile)
     */
    public void updateCompanyLogoIfPresent(Company company, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            commonUtilsService.checkImageFileType(file);
            uploadService.updateExistingUploadContent(company.getLogo(), file);
            log.info("Company logo updated for company with ID: {}", company.getId());
        }
    }

    /**
     * Validates the data for creating a new company.
     * Checks for a unique company name, tax ID, and ensures the current user
     * doesn't already have a company.
     *
     * @param companyRequestDto The DTO containing company details.
     * @param file              The logo file for the new company.
     * @param currentUser       The currently authenticated user.
     * @throws DuplicateResourceException if a company with the same name, tax ID,
     *                                    or for the same user already exists.
     * @see com.online_store.backend.api.company.service.CompanyService#createCompany(CompanyRequestDto,
     *      MultipartFile)
     */
    public void validateCompanyCreation(CompanyRequestDto companyRequestDto,
            MultipartFile file,
            User currentUser) {
        commonUtilsService.checkImageFileType(file);

        if (companyRepository.findByUser(currentUser).isPresent()) {
            log.warn("User '{}' attempted to create a second company.", currentUser.getEmail());
            throw new DuplicateResourceException("You can only create one company per user account.");
        }
        if (companyRepository.findByName(companyRequestDto.getName()).isPresent()) {
            log.warn("Company creation failed. Name '{}' is already in use.", companyRequestDto.getName());
            throw new DuplicateResourceException(
                    "Company with name '" + companyRequestDto.getName() + "' already exists.");
        }
        if (companyRepository.findByTaxId(companyRequestDto.getTaxId()).isPresent()) {
            log.warn("Company creation failed. Tax ID '{}' is already in use.", companyRequestDto.getTaxId());
            throw new DuplicateResourceException(
                    "Company with Tax ID '" + companyRequestDto.getTaxId() + "' already exists.");
        }
    }
}
