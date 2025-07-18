package com.online_store.backend.api.returnRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.returnRequest.entities.ReturnRequest;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {

}
