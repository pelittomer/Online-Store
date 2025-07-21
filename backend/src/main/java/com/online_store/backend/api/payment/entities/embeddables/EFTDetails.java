package com.online_store.backend.api.payment.entities.embeddables;

import java.time.LocalDateTime;

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
@ToString
public class EFTDetails {
    @Column(name = "eft_sender_name")
    private String senderName;

    @Column(name = "eft_sender_bank")
    private String senderBank;

    @Column(name = "eft_receiver_bank")
    private String receiverBank;

    @Column(name = "eft_transfer_date")
    private LocalDateTime transferDate;
}