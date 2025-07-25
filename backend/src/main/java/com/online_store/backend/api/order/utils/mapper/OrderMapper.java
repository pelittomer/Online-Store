package com.online_store.backend.api.order.utils.mapper;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.cart.entities.Cart;
import com.online_store.backend.api.cart.entities.CartItem;
import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.entities.OrderItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    public Order createOrderMapper(Cart dto, Address address) {
        BigDecimal totalAmount = dto.getCartItems()
                .stream()
                .map(item -> item.getProduct().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Set<OrderItem> orderItems = dto.getCartItems().stream()
                .map(this::orderItemMapper).collect(Collectors.toSet());
        Order order = Order.builder()
                .totalAmount(totalAmount)
                .address(address)
                .user(dto.getUser())
                .orderItems(orderItems)
                .build();

        orderItems.forEach((item) -> item.setOrder(order));
        return order;
    }

    private OrderItem orderItemMapper(CartItem dto) {
        return OrderItem.builder()
                .price(dto.getProduct().getPrice())
                .quantity(dto.getQuantity())
                .product(dto.getProduct())
                .productStock(dto.getProductStock())
                .build();
    }
}
