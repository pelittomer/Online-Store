package com.online_store.backend.api.company.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateStatusRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.repository.CompanyRepository;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.company.utils.mapper.CreateCompanyMapper;
import com.online_store.backend.api.company.utils.mapper.GetCompanyDetailsMapper;
import com.online_store.backend.api.company.utils.mapper.GetCompanyMapper;
import com.online_store.backend.api.company.utils.mapper.UpdateCompanyMapper;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing company-related business logic.
 * It handles the creation, updating, and retrieval of company information.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    // repositories
    private final CompanyRepository companyRepository;
    // utils
    private final CommonUtilsService commonUtilsService;
    private final CompanyUtilsService companyUtilsService;
    // mappers
    private final CreateCompanyMapper createCompanyMapper;
    private final UpdateCompanyMapper updateCompanyMapper;
    private final GetCompanyDetailsMapper getCompanyDetailsMapper;
    private final GetCompanyMapper getCompanyMapper;
    // services
    private final UploadService uploadService;

    /**
     * Creates a new company for the current authenticated user.
     * This method validates the company data, handles the logo upload, and saves
     * the new company.
     *
     * @param dto  The DTO containing company details.
     * @param file The logo image file for the company.
     * @return A success message upon successful creation.
     * @see com.online_store.backend.api.company.controller.CompanyController#createCompany(CompanyRequestDto,
     *      MultipartFile)
     */
    @Transactional
    public String createCompany(CompanyRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Creating new company for user: {}", currentUser.getEmail());

        validateCompanyCreation(dto, file, currentUser);

        Upload upload = uploadService.createFile(file);
        Company newCompany = createCompanyMapper.companyMapper(dto, upload, currentUser);
        companyRepository.save(newCompany);

        log.info("Company created successfully with name: '{}' by user: {}", newCompany.getName(),
                currentUser.getEmail());
        return "Company created succesfully.";
    }

    /**
     * Updates the details of the company belonging to the current user.
     * This includes updating company information and optionally the logo.
     *
     * @param dto  The DTO containing the updated company details.
     * @param file The new logo file, which can be null.
     * @return A success message upon successful update.
     * @see com.online_store.backend.api.company.controller.CompanyController#updateMyCompany(CompanyUpdateRequestDto,
     *      MultipartFile)
     */
    @Transactional
    public String updateMyCompany(CompanyUpdateRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Updating company details for user: {}", currentUser.getEmail());

        Company company = companyUtilsService.findCompanyByUser(currentUser);

        updateCompanyLogoIfPresent(company, file);
        updateCompanyMapper.companyMapper(company, dto);
        companyRepository.save(company);

        log.info("Company with ID: {} updated successfully by user: {}", company.getId(), currentUser.getEmail());
        return "Company updated succesfully.";
    }

    /**
     * Retrieves the private details of the company belonging to the current user.
     *
     * @return A {@link CompanyPrivateResponseDto} containing detailed company
     *         information.
     * @see com.online_store.backend.api.company.controller.CompanyController#getMyCompany()
     */
    @Transactional(readOnly = true)
    public CompanyPrivateResponseDto getMyCompany() {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Fetching company details for user: {}", currentUser.getEmail());

        Company company = companyUtilsService.findCompanyByUser(currentUser);
        log.debug("Found company details for user: {}", currentUser.getEmail());

        return getCompanyDetailsMapper.companyMapper(company);
    }

    /**
     * Updates the status of a specific company.
     * This is typically an administrative function to approve or reject a company.
     *
     * @param id  The ID of the company to update.
     * @param dto The DTO containing the new status and optional rejection reason.
     * @return A success message upon successful status update.
     * @see com.online_store.backend.api.company.controller.CompanyController#updateCompanyStatus(Long,
     *      CompanyUpdateStatusRequestDto)
     */
    @Transactional
    public String updateCompanyStatus(Long id, CompanyUpdateStatusRequestDto dto) {
        log.info("Updating status for company with ID: {}", id);
        Company company = companyUtilsService.findCompanyById(id);

        company.setStatus(dto.getStatus());
        company.setRejectionReason(dto.getRejectionReason());

        companyRepository.save(company);

        log.info("Company status updated to '{}' for company ID: {}", dto.getStatus(), id);
        return "Company status updated succesfully.";
    }

    /**
     * Retrieves the public details of a company by its ID.
     *
     * @param id The ID of the company.
     * @return A {@link CompanyResponseDto} containing public company information.
     * @see com.online_store.backend.api.company.controller.CompanyController#getCompanyById(Long)
     */
    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long id) {
        log.info("Fetching public company details for ID: {}", id);
        Company company = companyUtilsService.findCompanyById(id);
        log.debug("Found company with ID: {}", id);
        return getCompanyMapper.companyMapper(company);
    }

    /**
     * Retrieves a list of all companies.
     *
     * @return A list of {@link CompanyResponseDto} for all companies.
     * @see com.online_store.backend.api.company.controller.CompanyController#listAllCompanies()
     */
    @Transactional(readOnly = true)
    public List<CompanyResponseDto> listAllCompanies() {
        return companyRepository.findAll().stream()
                .map(getCompanyMapper::companyMapper)
                .collect(Collectors.toList());
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
    private void updateCompanyLogoIfPresent(Company company, MultipartFile file) {
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
    private void validateCompanyCreation(CompanyRequestDto companyRequestDto,
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