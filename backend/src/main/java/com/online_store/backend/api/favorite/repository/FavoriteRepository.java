package com.online_store.backend.api.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.favorite.entities.Favorite;
import com.online_store.backend.api.user.entities.User;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUser(User user);
}
