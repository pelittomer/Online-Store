package com.online_store.backend.api.company.utils.mapper;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.company.dto.request.CompanyUpdateRequestDto;
import com.online_store.backend.api.company.entities.Company;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCompanyMapper {

    public Company companyMapper(Company company, CompanyUpdateRequestDto dto) {
        Optional.ofNullable(dto.getName()).ifPresent(company::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(company::setDescription);
        Optional.ofNullable(dto.getWebsiteUrl()).ifPresent(company::setWebsiteUrl);
        Optional.ofNullable(dto.getPhone()).ifPresent(company::setPhone);
        Optional.ofNullable(dto.getEmail()).ifPresent(company::setEmail);
        return company;
    }
}
