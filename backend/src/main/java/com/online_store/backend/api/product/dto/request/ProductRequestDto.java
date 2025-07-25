package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private BigDecimal price;
    private Boolean isPublished;
    private Long brand;
    private Long shipper;
    private Long category;
    private ProductDetailRequestDto productDetail;
    @Valid
    @Builder.Default
    private List<ProductStockRequestDto> productStocks = new ArrayList<>();
}