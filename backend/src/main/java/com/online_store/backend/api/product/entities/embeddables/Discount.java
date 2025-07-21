package com.online_store.backend.api.product.entities.embeddables;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Discount {
    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

    @Column(name = "discount_start_date")
    private LocalDateTime startDate;

    @Column(name = "discount_end_date")
    private LocalDateTime endDate;

    @Column(name = "discount_applied_price", precision = 10, scale = 2)
    private BigDecimal appliedPrice;
}
