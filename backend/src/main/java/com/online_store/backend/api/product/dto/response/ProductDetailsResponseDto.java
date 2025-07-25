package com.online_store.backend.api.product.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.brand.dto.response.BrandResponseDto;
import com.online_store.backend.api.category.dto.response.CategoryResponseDto;
import com.online_store.backend.api.company.dto.response.CompanyResponseDto;
import com.online_store.backend.api.product.dto.base.DiscountDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponseDto {
    private Long id;
    private String name;
    private DiscountDto discount;
    private Boolean isPublished;
    @Builder.Default
    private List<Long> images = new ArrayList<>();
    private BrandResponseDto brand;
    private ShipperResponseDto shipper;
    private CompanyResponseDto company;
    private CategoryResponseDto category;
    private ProductDetailsDetailResponseDto productDetail;
    @Builder.Default
    private List<ProductStockResponseDto> productStocks = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}