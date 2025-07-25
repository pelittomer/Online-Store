package com.online_store.backend.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.product.entities.CriteriaOption;

public interface CriteriaOptionRepository extends JpaRepository<CriteriaOption, Long> {

}
