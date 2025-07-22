package com.online_store.backend.api.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.address.entities.Address;
import com.online_store.backend.api.user.entities.User;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
