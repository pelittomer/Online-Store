package com.online_store.backend.api.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.company.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
