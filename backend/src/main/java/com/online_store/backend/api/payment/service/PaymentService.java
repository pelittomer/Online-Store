package com.online_store.backend.api.payment.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.repository.OrderRepository;
import com.online_store.backend.api.payment.dto.request.PaymentRequestDto;
import com.online_store.backend.api.payment.entities.Payment;
import com.online_store.backend.api.payment.repository.PaymentRepository;
import com.online_store.backend.api.payment.utils.mapper.CreatePaymentMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CommonUtilsService commonUtilsService;
    private final OrderRepository orderRepository;
    private final CreatePaymentMapper createPaymentMapper;

    public String createPayment(PaymentRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        Order order = orderRepository.findById(dto.getOrder())
                .orElseThrow(() -> new EntityNotFoundException("Order with ID " + dto.getOrder() + " not found."));

        if (!order.getUser().equals(user)) {
            throw new AccessDeniedException("You are not authorized to make a payment for this order.");
        }
        Payment payment = createPaymentMapper.paymentMapper(order, dto);

        paymentRepository.save(payment);

        return "Payment successfully processed for Order ID: " + order.getId();
    }

}
