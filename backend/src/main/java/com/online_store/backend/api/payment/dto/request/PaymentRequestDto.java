package com.online_store.backend.api.payment.dto.request;

import com.online_store.backend.api.payment.entities.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull(message = "Order ID cannot be null.")
    @Min(value = 1, message = "Order ID must be a positive number.")
    private Long order;

    @NotNull(message = "Payment method cannot be null.")
    private PaymentMethod paymentMethod;

    @Valid
    @NotNull(message = "Payment details cannot be null.")
    private PaymentDetailsRequestDto paymentDetails;
}