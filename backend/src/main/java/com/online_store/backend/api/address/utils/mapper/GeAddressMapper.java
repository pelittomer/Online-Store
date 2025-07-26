package com.online_store.backend.api.address.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.entities.Address;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeAddressMapper {

    public AddressResponseDto addressMapper(Address dto) {
        return AddressResponseDto.builder()
                .id(dto.getId())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .neighborhood(dto.getNeighborhood())
                .street(dto.getStreet())
                .buildingNumber(dto.getBuildingNumber())
                .doorNumber(dto.getDoorNumber())
                .phone(dto.getPhone())
                .build();
    }
}
