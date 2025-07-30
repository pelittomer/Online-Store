package com.online_store.backend.api.product.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.entities.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProductMapper {

    public ProductResponseDto prouctMapper(Product dto) {
        List<Long> images = dto.getImages().stream()
                .map((image) -> image.getId()).toList();
        return ProductResponseDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .images(images)
                .brand(dto.getBrand().getName())
                .company(dto.getCompany().getName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
