package com.online_store.backend.api.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.order.dto.request.OrderRequestDto;
import com.online_store.backend.api.order.dto.response.OrderDetailsResponseDto;
import com.online_store.backend.api.order.dto.response.OrderResponseDto;
import com.online_store.backend.api.order.service.OrderService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing customer orders.
 * This controller provides endpoints for creating a new order,
 * listing all orders for the current user, and retrieving the details of a
 * specific order.
 */
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
        private final OrderService orderService;

        /**
         * Endpoint to create a new order from the items in the user's shopping cart.
         * The order is associated with a specific address.
         *
         * @param orderRequestDto The DTO containing the ID of the address for the
         *                        order.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         success message.
         */
        @PostMapping
        public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                orderService.createOrder(orderRequestDto)));
        }

        /**
         * Endpoint to list all orders placed by the current authenticated user.
         *
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
         *         list of {@link OrderResponseDto}
         *         for all orders.
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<OrderResponseDto>>> listUserOrders() {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                orderService.listUserOrders()));
        }

        /**
         * Endpoint to get the detailed information of a specific order by its ID.
         * The method ensures that the order belongs to the current user.
         *
         * @param id The ID of the order to retrieve.
         * @return A {@link ResponseEntity} with an {@link ApiResponse} containing the
         *         {@link OrderDetailsResponseDto}
         *         for the specified order.
         */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<OrderDetailsResponseDto>> getOrderDetails(@PathVariable Long id) {
                return ResponseEntity.ok(
                                ApiResponse.success("",
                                                orderService.getOrderDetails(id)));
        }
}
