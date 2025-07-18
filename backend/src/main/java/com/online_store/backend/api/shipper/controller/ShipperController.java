package com.online_store.backend.api.shipper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.shipper.service.ShipperService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shipper")
@RequiredArgsConstructor
public class ShipperController {
    private final ShipperService shipperService;

    @PostMapping
    public String createShipper(@RequestBody String shipperDetails) { // Likely a ShipperCreationDto
        // This function creates a new shipper (delivery company) in the system.
        // 'shipperDetails' would contain information like the shipper's name, contact
        // info, etc.
        return "New shipper created: " + shipperDetails;
    }

    @GetMapping
    public String listShippers() {
        // This function retrieves and lists all registered shippers.
        // It provides a comprehensive list of all delivery companies.
        return "List of all shippers will be returned here.";
    }
}
