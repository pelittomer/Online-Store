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

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("",
                        paymentService.createPayment(paymentRequestDto)));
    }
}
