package com.online_store.backend.api.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.favorite.entities.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
