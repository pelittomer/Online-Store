package com.online_store.backend.api.company.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyUtilsService {

    public Company createCompanyMapper(CompanyRequestDto dto, Upload upload, User currentUser) {
        return Company.builder()
                .name(dto.getName())
                .logo(upload)
                .description(dto.getDescription())
                .websiteUrl(dto.getWebsiteUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .taxId(dto.getTaxId())
                .taxtOffice(dto.getTaxOffice())
                .user(currentUser)
                .build();
    }

    public Company updateCompanyFromDto(Company company, CompanyUpdateRequestDto dto) {
        if (dto.getName() != null) {
            company.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            company.setDescription(dto.getDescription());
        }
        if (dto.getWebsiteUrl() != null) {
            company.setWebsiteUrl(dto.getWebsiteUrl());
        }
        if (dto.getPhone() != null) {
            company.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            company.setEmail(dto.getEmail());
        }
        return company;
    }

    public CompanyPrivateResponseDto companyPrivateResponseMapper(Company company) {
        return CompanyPrivateResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .logo(company.getLogo().getId())
                .description(company.getDescription())
                .websiteUrl(company.getWebsiteUrl())
                .phone(company.getPhone())
                .email(company.getEmail())
                .taxId(company.getTaxId())
                .taxtOffice(company.getTaxtOffice())
                .status(company.getStatus())
                .rejectionReason(company.getRejectionReason() != null ? company.getRejectionReason() : null)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    public CompanyResponseDto companyResponseMapper(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .logo(company.getLogo().getId())
                .description(company.getDescription())
                .websiteUrl(company.getWebsiteUrl())
                .phone(company.getPhone())
                .email(company.getEmail())
                .status(company.getStatus())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

}
