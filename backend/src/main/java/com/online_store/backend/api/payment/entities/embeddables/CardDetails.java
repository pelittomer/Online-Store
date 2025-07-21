package com.online_store.backend.api.payment.entities.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "cardNumber" })
public class CardDetails {
    @Column(name = "card_last_four_digits", length = 4)
    private String cardNumber;

    @Column(name = "card_holder_name", length = 255)
    private String cardHolderName;

    @Column(name = "card_expiry_month", length = 2)
    private String expiryMonth;

    @Column(name = "card_expiry_year", length = 4)
    private String expiryYear;
}
