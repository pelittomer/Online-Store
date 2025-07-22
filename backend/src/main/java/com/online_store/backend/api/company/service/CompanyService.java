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
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.upload.service.UploadService;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.exception.DuplicateResourceException;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CommonUtilsService commonUtilsService;
    private final CompanyUtilsService companyUtilsService;
    private final UploadService uploadService;

    @Transactional
    public String createCompany(CompanyRequestDto companyRequestDto, MultipartFile file) {
        commonUtilsService.checkImageFileType(file);
        User currentUser = commonUtilsService.getCurrentUser();

        if (companyRepository.findByUser(currentUser).isPresent()) {
            throw new DuplicateResourceException("You can only create one company per user account.");
        }

        if (companyRepository.findByName(companyRequestDto.getName()).isPresent()) {
            throw new DuplicateResourceException(
                    "Company with name '" + companyRequestDto.getName() + "' already exists.");
        }

        if (companyRepository.findByTaxId(companyRequestDto.getTaxId()).isPresent()) {
            throw new DuplicateResourceException(
                    "Company with Tax ID '" + companyRequestDto.getTaxId() + "' already exists.");
        }

        Upload upload = uploadService.createFile(file);

        Company newCompany = companyUtilsService.createCompanyMapper(companyRequestDto, upload, currentUser);

        companyRepository.save(newCompany);

        return "Company created succesfully.";
    }

    @Transactional
    public String updateMyCompany(CompanyUpdateRequestDto companyUpdateRequestDto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();

        Company company = companyRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Company not found for the current user."));

        if (file != null && !file.isEmpty()) {
            commonUtilsService.checkImageFileType(file);
            uploadService.updateExistingUploadContent(company.getLogo(), file);
        }

        companyUtilsService.updateCompanyFromDto(company, companyUpdateRequestDto);

        companyRepository.save(company);

        return "Company updated succesfully.";
    }

    @Transactional(readOnly = true)
    public CompanyPrivateResponseDto getMyCompany() {
        User currentUser = commonUtilsService.getCurrentUser();

        Company company = companyRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Company not found for the current user."));

        return companyUtilsService.companyPrivateResponseMapper(company);
    }

    @Transactional
    public String updateCompanyStatus(Long id, CompanyUpdateStatusRequestDto companyUpdateStatusRequestDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with ID " + id + " not found."));

        company.setStatus(companyUpdateStatusRequestDto.getStatus());
        company.setRejectionReason(companyUpdateStatusRequestDto.getRejectionReason());

        companyRepository.save(company);

        return "Company status updated succesfully.";
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with ID " + id + " not found."));

        return companyUtilsService.companyResponseMapper(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> listAllCompanies() {
        return companyRepository.findAll().stream()
                .map(companyUtilsService::companyResponseMapper)
                .collect(Collectors.toList());
    }

}