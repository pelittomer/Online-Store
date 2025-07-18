package com.online_store.backend.api.brand.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.brand.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

}
