package com.online_store.backend.api.order.utils;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility service for Order-related operations.
 * This component provides helper methods for retrieving order entities
 * with consistent error handling and logging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUtilsService {
    // repositories
    private final OrderRepository orderRepository;

    /**
     * Finds an order by its unique identifier.
     *
     * @param orderId The ID of the order to be retrieved.
     * @return The {@link Order} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if no order with the given ID exists.
     * @see com.online_store.backend.api.payment.service.PaymentService#createPayment(com.online_store.backend.api.payment.dto.request.PaymentRequestDto)
     */
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with ID {} not found.", orderId);
                    return new EntityNotFoundException("Order not found!");
                });
    }
}
