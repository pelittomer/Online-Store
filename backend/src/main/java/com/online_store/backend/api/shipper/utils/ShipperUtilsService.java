package com.online_store.backend.api.shipper.utils;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.online_store.backend.api.shipper.entities.Shipper;
import com.online_store.backend.api.shipper.repository.ShipperRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipperUtilsService {
    // repositories
    private final ShipperRepository shipperRepository;

    public String generateUniqueApiKey() {
        String apiKey;
        do {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        } while (shipperRepository.findByApiKey(apiKey).isPresent());
        return apiKey;
    }

    public Shipper findShipperById(Long shipperId) {
        return shipperRepository.findById(shipperId)
                .orElseThrow(() -> {
                    log.warn("Shipper with ID {} not found.", shipperId);
                    return new EntityNotFoundException("Shipper not found!");
                });
    }
}
