package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.RecipeIngredient;
import com.on_bapsang.backend.entity.RecipeIngredientKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeIngredientRepository
        extends JpaRepository<RecipeIngredient, RecipeIngredientKey> {

    @Query("SELECT im.name FROM RecipeIngredient ri "
            + " JOIN IngredientMaster im ON ri.id.ingredientId = im.ingredientId "
            + " WHERE ri.id.recipeId = :recipeId")
    List<String> findIngredientNamesByRecipeId(@Param("recipeId") String recipeId);
}