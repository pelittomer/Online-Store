package com.online_store.backend.api.company.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.company.entities.Company;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCompanyMapper {

    public CompanyResponseDto companyMapper(Company company) {
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
