package com.online_store.backend.api.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online_store.backend.api.category.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT MAX(c.rightValue) FROM Category c")
    Long findMaxRightValue();

    @Modifying
    @Query("UPDATE Category c SET c.rightValue = c.rightValue + :increment WHERE c.rightValue >= :value")
    void incrementRightValuesGreaterThanOrEqualTo(@Param("value") Integer value, @Param("increment") Integer increment);

    @Modifying
    @Query("UPDATE Category c SET c.leftValue = c.leftValue + :increment WHERE c.leftValue > :value")
    void incrementLeftValuesGreaterThan(@Param("value") Integer value, @Param("increment") Integer increment);

    List<Category> findAllByOrderByLeftValueAsc();

    @Query("SELECT c FROM Category c WHERE c.rightValue = c.leftValue + 1")
    List<Category> findLeafCategories();

    List<Category> findByParentIsNull();
}
