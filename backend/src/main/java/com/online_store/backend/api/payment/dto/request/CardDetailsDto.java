package com.online_store.backend.api.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailsDto {
    @NotBlank(message = "Card number cannot be blank.")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits.")
    private String cardNumber;

    @NotBlank(message = "Card holder name cannot be blank.")
    private String cardHolderName;

    @NotBlank(message = "Expiry month cannot be blank.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Invalid expiry month format (MM).")
    private String expiryMonth;

    @NotBlank(message = "Expiry year cannot be blank.")
    @Pattern(regexp = "^(202[4-9]|20[3-9][0-9]|2[1-9][0-9][0-9]|3[0-9]{3})$", message = "Invalid expiry year.")
    private String expiryYear;
}
