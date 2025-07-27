package com.online_store.backend.api.shipper.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.entities.Shipper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetShipperMapper {

    public ShipperResponseDto shipperMapper(Shipper shipper) {
        return ShipperResponseDto.builder()
                .id(shipper.getId())
                .name(shipper.getName())
                .logo(shipper.getLogo().getId())
                .websiteUrl(shipper.getWebsiteUrl())
                .phone(shipper.getPhone())
                .email(shipper.getEmail())
                .address(shipper.getAddress())
                .isActive(shipper.isActive())
                .createdAt(shipper.getCreatedAt())
                .build();
    }
}
