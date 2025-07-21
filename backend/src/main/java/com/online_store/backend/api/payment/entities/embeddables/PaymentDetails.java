package com.online_store.backend.api.payment.entities.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
@ToString(exclude = { "cardDetails" })
public class PaymentDetails {
    @Embedded
    private CardDetails cardDetails;

    @Embedded
    private EFTDetails eftDetails;
}
