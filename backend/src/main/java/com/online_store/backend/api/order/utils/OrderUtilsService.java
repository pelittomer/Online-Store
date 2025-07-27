package com.online_store.backend.api.order.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUtilsService {
    //repositories
    private final OrderRepository orderRepository;

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with ID {} not found.", orderId);
                    return new EntityNotFoundException("Order not found!");
                });
    }
}
