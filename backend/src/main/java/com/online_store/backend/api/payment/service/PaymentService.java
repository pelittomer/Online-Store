package com.online_store.backend.api.payment.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

}
