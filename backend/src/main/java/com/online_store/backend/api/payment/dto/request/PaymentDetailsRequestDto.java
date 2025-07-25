package com.online_store.backend.api.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsRequestDto {
    private CardDetailsDto cardDetails;
    private EftDetailsDto eftDetails;
}
