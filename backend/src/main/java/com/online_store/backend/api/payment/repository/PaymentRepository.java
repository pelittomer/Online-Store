package com.online_store.backend.api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.payment.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
