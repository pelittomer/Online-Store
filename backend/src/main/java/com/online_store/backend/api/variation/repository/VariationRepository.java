package com.online_store.backend.api.variation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.variation.entities.Variation;

public interface VariationRepository extends JpaRepository<Variation, Long> {

}
