package com.online_store.backend.api.brand.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.brand.dto.request.BrandRequestDto;
import com.online_store.backend.api.brand.entities.Brand;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreteBrandMapper {

    public Brand brandMapper(BrandRequestDto dto, Upload logo) {
        return Brand.builder()
                .name(dto.getName())
                .logo(logo)
                .build();
    }
}
