package com.online_store.backend.api.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean isPublished;
    @Builder.Default
    List<Long> images = new ArrayList<>();
    private String brand;
    private String company;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
