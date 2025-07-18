package com.online_store.backend.api.address.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.online_store.backend.api.address.service.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public String addAddress(@RequestBody String addressDetails) {
        // Business logic to add a new address for the user will be here.
        // 'addressDetails' would typically be a DTO representing the address
        // information.
        return "Address added: " + addressDetails;
    }

    @GetMapping
    public String listAddresses() {
        // Business logic to retrieve and list all addresses for the user will be here.
        return "List of user addresses will be returned here.";
    }

    @DeleteMapping("/{id}")
    public String deleteAddress(@PathVariable String id) {
        // Business logic to delete a specific address by its ID will be here.
        return "Address with ID " + id + " deleted.";
    }
}
