package com.online_store.backend.api.company.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {
    @NotBlank(message = "Company name cannot be blank")
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String websiteUrl;

    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Tax ID cannot be blank")
    @Size(min = 10, max = 11, message = "Tax ID must be between 10 and 11 characters (e.g., Turkish tax ID)")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Tax ID must contain only digits")
    private String taxId;

    @NotBlank(message = "Tax office cannot be blank")
    @Size(max = 255, message = "Tax office cannot exceed 255 characters")
    private String taxOffice;
}
