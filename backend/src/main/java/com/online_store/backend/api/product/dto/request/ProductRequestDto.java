package com.online_store.backend.api.product.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Product name cannot be blank.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "isPublished status cannot be null.")
    private Boolean isPublished;

    @NotNull(message = "Brand ID cannot be null.")
    @Min(value = 1, message = "Brand ID must be a positive number.")
    private Long brand;

    @NotNull(message = "Shipper ID cannot be null.")
    @Min(value = 1, message = "Shipper ID must be a positive number.")
    private Long shipper;

    @NotNull(message = "Category ID cannot be null.")
    @Min(value = 1, message = "Category ID must be a positive number.")
    private Long category;

    @NotNull(message = "Product details cannot be null.")
    @Valid
    private ProductDetailRequestDto productDetail;

    @Valid
    @Builder.Default
    private List<ProductStockRequestDto> productStocks = new ArrayList<>();
}