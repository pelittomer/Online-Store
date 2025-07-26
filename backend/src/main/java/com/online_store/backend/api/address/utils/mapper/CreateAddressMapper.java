package com.online_store.backend.api.address.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.address.dto.request.AddressRequestDto;
import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.user.entities.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateAddressMapper {

    public Address addressMapper(AddressRequestDto dto, User currentUser) {
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
