package com.on_bapsang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class IngredientDetailDto {

    @JsonProperty("ingredient_id")
    private String ingredientId;

    private String name;
    private String amount;
}
