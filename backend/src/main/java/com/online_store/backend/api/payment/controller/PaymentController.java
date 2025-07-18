package com.online_store.backend.api.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public String createPayment(@RequestBody String paymentDetails) { // Likely a PaymentRequestDto
        // This function initiates a new payment process.
        // 'paymentDetails' would typically include order ID, amount, payment method
        // details, etc.
        return "New payment initiated with details: " + paymentDetails;
    }
}
