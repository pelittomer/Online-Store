package com.online_store.backend.api.shipper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.shipper.entities.Shipper;

public interface ShipperRepository extends JpaRepository<Shipper, Long> {
    Optional<Shipper> findByName(String name);

    Optional<Shipper> findByApiKey(String apiKey);
}
