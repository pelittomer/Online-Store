package com.online_store.backend.api.variation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.variation.entities.Variation;
import java.util.List;

import com.online_store.backend.api.category.entities.Category;

public interface VariationRepository extends JpaRepository<Variation, Long> {
    List<Variation> findByCategoryIsNull();

    List<Variation> findByCategory(Category currentCategory);
}
