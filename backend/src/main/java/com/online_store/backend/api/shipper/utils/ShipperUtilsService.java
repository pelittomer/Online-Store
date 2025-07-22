package com.online_store.backend.api.shipper.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.shipper.dto.request.ShipperRequestDto;
import com.online_store.backend.api.shipper.dto.response.ShipperResponseDto;
import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;
import com.online_store.backend.api.upload.entities.Upload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShipperUtilsService {
    private final ShipperRepository shipperRepository;

    /**
     * Maps a ShipperRequestDto and an Upload entity to a Shipper entity.
     * Also generates a unique API key for the shipper.
     *
     * @param dto    The ShipperRequestDto containing shipper details.
     * @param upload The Upload entity for the shipper's logo.
     * @return A new Shipper entity.
     */
    public Shipper shipperRequestMapper(ShipperRequestDto dto, Upload upload) {
        return Shipper.builder()
                .name(dto.getName())
                .logo(upload)
                .websiteUrl(dto.getWebsiteUrl())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .apiKey(this.generateUniqueApiKey())
                .build();
    }

    /**
     * Maps a Shipper entity to a ShipperResponseDto.
     *
     * @param shipper The Shipper entity.
     * @return A ShipperResponseDto.
     */
    public ShipperResponseDto shipperResponseMapper(Shipper shipper) {
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

    /**
     * Generates a unique API key (UUID without hyphens) and ensures its uniqueness
     * in the database.
     *
     * @return A unique API key string.
     */
    public String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        } while (shipperRepository.findByApiKey(apiKey).isPresent());
        return apiKey;
    }
}
