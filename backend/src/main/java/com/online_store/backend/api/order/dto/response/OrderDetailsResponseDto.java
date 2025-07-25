package com.online_store.backend.api.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class OrderDetailsResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private AddressResponseDto address;
    @Builder.Default
    private List<OrderItemResponseDto> orderItems = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
