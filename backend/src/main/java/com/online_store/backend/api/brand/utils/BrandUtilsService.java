package com.online_store.backend.api.brand.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrandUtilsService {

    public Brand createBrandMapper(BrandRequestDto dto, Upload logo) {
        return Brand.builder()
                .name(dto.getName())
                .logo(logo)
                .build();
    }

    public BrandResponseDto brandResponseMapper(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .logo(brand.getLogo().getId())
                .createdAt(brand.getCreatedAt())
                .build();
    }

}
