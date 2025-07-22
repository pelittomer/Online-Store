package com.online_store.backend.api.address.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private Long id;
    private String city;
    private String district;
    private String neighborhood;
    private String street;
    private String buildingNumber;
    private String doorNumber;
    private String phone;
}
