package com.online_store.backend.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.review.entities.Review;
import java.util.Optional;

import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.product.entities.Product;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndProduct(User user, Product product);

    List<Review> findByProduct(Product product);
}
