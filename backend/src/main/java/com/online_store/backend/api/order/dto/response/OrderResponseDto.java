package com.online_store.backend.api.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.order.entities.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private AddressResponseDto address;
    private Integer orderItemCount;
    private Long shippedItemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
