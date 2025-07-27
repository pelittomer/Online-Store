package com.online_store.backend.api.shipper.utils.mapper;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.utils.ShipperUtilsService;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateShipperMapper {
    private final ShipperUtilsService shipperUtilsService;

    public Shipper shipperMapper(ShipperRequestDto dto, Upload upload) {
        return Shipper.builder()
                .name(dto.getName())
                .logo(upload)
                .websiteUrl(dto.getWebsiteUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .apiKey(shipperUtilsService.generateUniqueApiKey())
                .build();
    }
}
