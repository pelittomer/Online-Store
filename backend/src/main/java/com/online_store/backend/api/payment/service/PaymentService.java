package com.online_store.backend.api.payment.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.utils.OrderUtilsService;
import com.online_store.backend.api.payment.dto.request.PaymentRequestDto;
import com.online_store.backend.api.payment.entities.Payment;
import com.online_store.backend.api.payment.repository.PaymentRepository;
import com.online_store.backend.api.payment.utils.mapper.CreatePaymentMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    //repositories
    private final PaymentRepository paymentRepository;
    //utils
    private final CommonUtilsService commonUtilsService;
    private final OrderUtilsService orderUtilsService;
    //mappers
    private final CreatePaymentMapper createPaymentMapper;

    public String createPayment(PaymentRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        log.info("Attempting to process payment for order ID: {} by user: {}", dto.getOrder(), user.getEmail());

        Order order = orderUtilsService.findOrderById(dto.getOrder());

        if (!order.getUser().equals(user)) {
            log.warn("Unauthorized payment attempt for order ID: {} by user: {}", order.getId(), user.getEmail());
            throw new AccessDeniedException("You are not authorized to make a payment for this order.");
        }
        Payment payment = createPaymentMapper.paymentMapper(order, dto);

        paymentRepository.save(payment);

        log.info("Payment successfully processed for Order ID: {}. Payment ID: {}", order.getId(), payment.getId());
        return "Payment successfully processed for Order ID: " + order.getId();
    }

}
