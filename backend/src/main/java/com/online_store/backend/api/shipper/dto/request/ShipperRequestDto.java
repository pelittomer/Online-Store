package com.online_store.backend.api.shipper.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipperRequestDto {
    @NotBlank(message = "Shipper name cannot be blank")
    @Size(max = 100, message = "Shipper name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Website URL cannot be blank")
    private String websiteUrl;

    @NotBlank(message = "Phone number cannot be blank")
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;
}
