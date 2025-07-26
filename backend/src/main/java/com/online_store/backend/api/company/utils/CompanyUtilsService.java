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

    public Company getCurrentUserCompany() {
        User currentUser = commonUtilsService.getCurrentUser();
        Company company = companyRepository.findByUser(currentUser)
                .orElseThrow(() -> {
                    log.warn("Company not found for current user: {}", currentUser.getEmail());
                    return new EntityNotFoundException("Company not found!");
                });
        return company;
    }

    public Company findCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found with ID: {}", id);
                    return new EntityNotFoundException("Company with ID " + id + " not found.");
                });
    }

    public Company findCompanyByUser(User user) {
        return companyRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.warn("Company not found for user: {}", user.getEmail());
                    return new EntityNotFoundException("Company not found for the current user.");
                });
    }

    public void updateCompanyLogoIfPresent(Company company, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            commonUtilsService.checkImageFileType(file);
            uploadService.updateExistingUploadContent(company.getLogo(), file);
            log.info("Company logo updated for company with ID: {}", company.getId());
        }
    }

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
