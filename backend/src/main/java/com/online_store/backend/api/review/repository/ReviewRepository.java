package com.online_store.backend.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.review.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
