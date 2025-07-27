package com.online_store.backend.api.brand.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.brand.entities.Brand;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetBrandMapper {

    public BrandResponseDto brandMapper(Brand dto) {
        return BrandResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .logo(dto.getLogo().getId())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
