package com.online_store.backend.api.payment.utils.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.payment.dto.request.CardDetailsDto;
import com.online_store.backend.api.payment.dto.request.EftDetailsDto;
import com.online_store.backend.api.payment.dto.request.PaymentDetailsRequestDto;
import com.online_store.backend.api.payment.dto.request.PaymentRequestDto;
import com.online_store.backend.api.payment.entities.Payment;
import com.online_store.backend.api.payment.entities.PaymentStatus;
import com.online_store.backend.api.payment.entities.embeddables.CardDetails;
import com.online_store.backend.api.payment.entities.embeddables.EFTDetails;
import com.online_store.backend.api.payment.entities.embeddables.PaymentDetails;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreatePaymentMapper {

    public Payment paymentMapper(Order orderDto, PaymentRequestDto paymentDto) {
        PaymentDetails paymentDetails = paymentStatusMapper(paymentDto.getPaymentDetails());
        return Payment.builder()
                .paymentMethod(paymentDto.getPaymentMethod())
                .paymentDetails(paymentDetails)
                .paymentStatus(PaymentStatus.PENDING)
                .amount(orderDto.getTotalAmount())
                .order(orderDto)
                .paymentDate(LocalDateTime.now())
                .build();
    }

    private PaymentDetails paymentStatusMapper(PaymentDetailsRequestDto dto) {
        CardDetails cardDetails = null;
        EFTDetails eftDetails = null;
        if (dto.getCardDetails() != null) {
            cardDetails = cardDetailsMapper(dto.getCardDetails());
        }
        if (dto.getEftDetails() != null) {
            eftDetails = eftDetailsMapper(dto.getEftDetails());
        }
        return PaymentDetails.builder()
                .cardDetails(cardDetails)
                .eftDetails(eftDetails)
                .build();
    }

    private CardDetails cardDetailsMapper(CardDetailsDto dto) {
        return CardDetails.builder()
                .cardNumber(dto.getCardNumber())
                .cardHolderName(dto.getCardHolderName())
                .expiryMonth(dto.getExpiryMonth())
                .expiryYear(dto.getExpiryYear())
                .build();
    }

    private EFTDetails eftDetailsMapper(EftDetailsDto dto) {
        return EFTDetails.builder()
                .senderName(dto.getSenderName())
                .senderBank(dto.getSenderBank())
                .receiverBank(dto.getReceiverBank())
                .build();
    }
}
