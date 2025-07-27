package com.online_store.backend.api.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.payment.dto.request.PaymentRequestDto;
import com.online_store.backend.api.payment.service.PaymentService;
import com.online_store.backend.common.exception.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling payment-related operations.
 * This controller provides an endpoint for processing payments for orders.
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Endpoint to create and process a new payment for a specified order.
     * The request body contains the details necessary to complete the payment.
     *
     * @param paymentRequestDto The DTO containing the order ID and payment
     *                          information.
     * @return A {@link ResponseEntity} with an {@link ApiResponse} containing a
     *         success message.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        paymentService.createPayment(paymentRequestDto)));
    }
}
