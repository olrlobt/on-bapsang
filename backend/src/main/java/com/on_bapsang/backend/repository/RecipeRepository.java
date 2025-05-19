package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = "SELECT * FROM `Recipe` WHERE name LIKE CONCAT(:keyword, '%') COLLATE utf8mb4_general_ci LIMIT 10", nativeQuery = true)
    List<Recipe> findTop10ByNameStartingWithIgnoreCase(@Param("keyword") String keyword);
}
