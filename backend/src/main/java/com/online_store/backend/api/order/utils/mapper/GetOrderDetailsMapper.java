package com.online_store.backend.api.order.utils.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.utils.mapper.GeAddressMapper;
import com.online_store.backend.api.order.dto.response.OrderDetailsResponseDto;
import com.online_store.backend.api.order.dto.response.OrderItemResponseDto;
import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.entities.OrderItem;
import com.online_store.backend.api.product.dto.response.ProductResponseDto;
import com.online_store.backend.api.product.dto.response.StockVariationResponseDto;
import com.online_store.backend.api.product.utils.mapper.GetProductDetailsMapper;
import com.online_store.backend.api.product.utils.mapper.GetProductMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOrderDetailsMapper {
    private final GeAddressMapper getAddressMapper;
    private final GetProductMapper getProductMapper;
    private final GetProductDetailsMapper getProductDetailsMapper;

    public OrderDetailsResponseDto orderDetailsMapper(Order dto) {
        AddressResponseDto address = getAddressMapper.addressMapper(dto.getAddress());
        List<OrderItemResponseDto> orderItems = dto.getOrderItems().stream()
                .map(this::orderItemMapepr).toList();
        return OrderDetailsResponseDto.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .address(address)
                .orderItems(orderItems)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    private OrderItemResponseDto orderItemMapepr(OrderItem dto) {
        ProductResponseDto product = getProductMapper.prouctMapper(dto.getProduct());
        List<StockVariationResponseDto> stockVariations = dto.getProduct().getProductStocks().stream()
                .flatMap(item -> item.getStockVariations().stream())
                .map(getProductDetailsMapper::stockVariationMapper)
                .toList();
        return OrderItemResponseDto.builder()
                .id(dto.getId())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .status(dto.getOrderStatus())
                .product(product)
                .stockVariation(stockVariations)
                .build();
    }
}
