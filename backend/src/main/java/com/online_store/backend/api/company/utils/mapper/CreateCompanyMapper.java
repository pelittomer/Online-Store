package com.online_store.backend.api.company.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.company.dto.request.CompanyRequestDto;
import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.upload.entities.Upload;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateCompanyMapper {

    public Company companyMapper(CompanyRequestDto dto, Upload upload, User user) {
        return Company.builder()
                .name(dto.getName())
                .logo(upload)
                .description(dto.getDescription())
                .websiteUrl(dto.getWebsiteUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .taxId(dto.getTaxId())
                .taxtOffice(dto.getTaxOffice())
                .user(user)
                .build();
    }

}
