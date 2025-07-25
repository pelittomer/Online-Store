package com.online_store.backend.api.product.dto.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private BigDecimal discountPercentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal appliedPrice;
}
