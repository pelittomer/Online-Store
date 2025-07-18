package com.online_store.backend.api.shipper.service;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.shipper.repository.ShipperRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperService {
    private final ShipperRepository shipperRepository;

}
