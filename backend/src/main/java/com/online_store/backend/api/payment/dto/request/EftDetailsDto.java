package com.online_store.backend.api.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EftDetailsDto {
    private String senderName;
    private String senderBank;
    private String receiverBank;
}
