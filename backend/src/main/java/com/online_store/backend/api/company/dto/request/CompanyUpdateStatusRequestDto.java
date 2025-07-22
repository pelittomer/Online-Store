package com.online_store.backend.api.company.dto.request;

import com.online_store.backend.api.company.entities.CompanyStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateStatusRequestDto {
    @NotNull(message = "Company status cannot be null") 
    private CompanyStatus status;

    @Size(max = 500, message = "Rejection reason cannot exceed 500 characters")
    private String rejectionReason;
}