package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository extends JpaRepository<Recipe, String> {
}