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
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @Transactional
    public String createCompany(CompanyRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Creating new company for user: {}", currentUser.getEmail());

        companyUtilsService.validateCompanyCreation(dto, file, currentUser);

        Upload upload = uploadService.createFile(file);
        Company newCompany = createCompanyMapper.companyMapper(dto, upload, currentUser);
        companyRepository.save(newCompany);

        log.info("Company created successfully with name: '{}' by user: {}", newCompany.getName(),
                currentUser.getEmail());
        return "Company created succesfully.";
    }

    @Transactional
    public String updateMyCompany(CompanyUpdateRequestDto dto, MultipartFile file) {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Updating company details for user: {}", currentUser.getEmail());

        Company company = companyUtilsService.findCompanyByUser(currentUser);

        companyUtilsService.updateCompanyLogoIfPresent(company, file);
        updateCompanyMapper.companyMapper(company, dto);
        companyRepository.save(company);

        log.info("Company with ID: {} updated successfully by user: {}", company.getId(), currentUser.getEmail());
        return "Company updated succesfully.";
    }

    @Transactional(readOnly = true)
    public CompanyPrivateResponseDto getMyCompany() {
        User currentUser = commonUtilsService.getCurrentUser();
        log.info("Fetching company details for user: {}", currentUser.getEmail());

        Company company = companyUtilsService.findCompanyByUser(currentUser);
        log.debug("Found company details for user: {}", currentUser.getEmail());

        return getCompanyDetailsMapper.companyMapper(company);
    }

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

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long id) {
        log.info("Fetching public company details for ID: {}", id);
        Company company = companyUtilsService.findCompanyById(id);
        log.debug("Found company with ID: {}", id);
        return getCompanyMapper.companyMapper(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> listAllCompanies() {
        return companyRepository.findAll().stream()
                .map(getCompanyMapper::companyMapper)
                .collect(Collectors.toList());
    }
}