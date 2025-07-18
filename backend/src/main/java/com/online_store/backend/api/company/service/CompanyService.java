package com.online_store.backend.api.company.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

}
