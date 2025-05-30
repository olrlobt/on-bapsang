package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeRepository extends JpaRepository<Recipe, String> {
    Page<Recipe> findByNameContaining(String name, Pageable pageable);

    Page<Recipe> findByMaterialTypeContaining(String category, Pageable pageable);
}