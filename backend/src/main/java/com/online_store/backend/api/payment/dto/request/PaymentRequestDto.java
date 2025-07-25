package com.online_store.backend.api.payment.dto.request;

import com.online_store.backend.api.payment.entities.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private Long order;
    private PaymentMethod paymentMethod;
    private PaymentDetailsRequestDto paymentDetails;
}