package com.online_store.backend.api.address.dto.request;

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
public class AddressRequestDto {
    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "District cannot be blank")
    private String district;

    private String neighborhood;

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "Building number cannot be blank")
    @Size(max = 10, message = "Building number cannot exceed 10 characters")
    private String buildingNumber;

    private String doorNumber;

    @NotBlank(message = "Phone number cannot be blank")
    private String phone;
}
