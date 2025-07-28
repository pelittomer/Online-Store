package com.online_store.backend.api.company.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.company.dto.response.CompanyPrivateResponseDto;
import com.online_store.backend.api.company.entities.Company;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetCompanyDetailsMapper {

    public CompanyPrivateResponseDto companyMapper(Company dto) {
        return CompanyPrivateResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .logo(dto.getLogo().getId())
                .description(dto.getDescription())
                .websiteUrl(dto.getWebsiteUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .taxId(dto.getTaxId())
                .taxtOffice(dto.getTaxtOffice())
                .status(dto.getStatus())
                .rejectionReason(dto.getRejectionReason())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
