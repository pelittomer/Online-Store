package com.online_store.backend.api.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_store.backend.api.category.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
