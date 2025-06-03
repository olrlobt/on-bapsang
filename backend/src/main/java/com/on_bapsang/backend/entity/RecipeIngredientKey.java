package com.on_bapsang.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RecipeIngredientKey implements Serializable {

    @Column(name = "recipe_id", length = 32)
    private String recipeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;
}