package com.online_store.backend.api.company.dto.response;

import java.time.LocalDateTime;

import com.online_store.backend.api.company.entities.CompanyStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPrivateResponseDto {
    private Long id;
    private String name;
    private Long logo;
    private String description;
    private String websiteUrl;
    private String phone;
    private String email;
    private String taxId;
    private String taxtOffice;
    private CompanyStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
