package com.online_store.backend.api.address.utils;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.dto.response.AddressResponseDto;
import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressUtilsService {

    public AddressResponseDto addressResponseMapper(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .district(address.getDistrict())
                .neighborhood(address.getNeighborhood())
                .street(address.getStreet())
                .buildingNumber(address.getBuildingNumber())
                .doorNumber(address.getDoorNumber())
                .phone(address.getPhone())
                .build();
    }

    public Address addressRequestMapper(AddressRequestDto dto, User currentUser) {
        return Address.builder()
                .city(dto.getCity())
                .district(dto.getDistrict())
                .neighborhood(dto.getNeighborhood())
                .street(dto.getStreet())
                .buildingNumber(dto.getBuildingNumber())
                .doorNumber(dto.getDoorNumber())
                .phone(dto.getPhone())
                .user(currentUser)
                .build();
    }
}
