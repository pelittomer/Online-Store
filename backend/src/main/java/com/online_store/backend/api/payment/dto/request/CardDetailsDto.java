package com.online_store.backend.api.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailsDto {
    private String cardNumber;
    private String cardHolderName;
    private String expiryMonth;
    private String expiryYear;
}
