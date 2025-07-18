package com.online_store.backend.api.address.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.address.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

}
