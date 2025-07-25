package com.online_store.backend.api.cart.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.online_store.backend.api.product.dto.base.DiscountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponseDto {
    private Long id;
    private String name;
    private DiscountDto discount;
    private Boolean isPublished;
    @Builder.Default
    private List<Long> images = new ArrayList<>();
    private String brand;
    private String company;
    private String shipper;
}
