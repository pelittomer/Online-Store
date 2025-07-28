package com.online_store.backend.api.payment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsRequestDto {
    @Valid
    private CardDetailsDto cardDetails;

    @Valid
    private EftDetailsDto eftDetails;

    @AssertTrue(message = "Either card details or EFT details must be provided.")
    private boolean isPaymentDetailsProvided() {
        return (cardDetails != null && eftDetails == null) || (cardDetails == null && eftDetails != null);
    }
}
