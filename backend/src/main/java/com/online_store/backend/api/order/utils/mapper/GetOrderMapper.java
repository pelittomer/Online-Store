package com.online_store.backend.api.order.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.utils.mapper.GeAddressMapper;
import com.online_store.backend.api.order.dto.response.OrderResponseDto;
import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.entities.OrderStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOrderMapper {
    private final GeAddressMapper getAddressMapper;

    public OrderResponseDto orderResponseMapper(Order dto) {
        AddressResponseDto address = getAddressMapper.addressMapper(dto.getAddress());
        Integer orderItemCount = dto.getOrderItems().size();
        Long shippedItemCount = dto.getOrderItems().stream()
                .filter((item) -> item.getOrderStatus().equals(OrderStatus.SHIPPED))
                .count();
        return OrderResponseDto.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .address(address)
                .orderItemCount(orderItemCount)
                .shippedItemCount(shippedItemCount)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}