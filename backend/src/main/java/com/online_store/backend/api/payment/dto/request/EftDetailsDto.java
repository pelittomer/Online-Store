package com.online_store.backend.api.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EftDetailsDto {
    @NotBlank(message = "Sender name cannot be blank.")
    private String senderName;

    @NotBlank(message = "Sender bank cannot be blank.")
    private String senderBank;

    @NotBlank(message = "Receiver bank cannot be blank.")
    private String receiverBank;
}
