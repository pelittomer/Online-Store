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

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        orderService.createOrder(orderRequestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> listUserOrders() {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        orderService.listUserOrders()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDetailsResponseDto>> getOrderDetails(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        orderService.getOrderDetails(id)));
    }
}
