package com.online_store.backend.api.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.address.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
