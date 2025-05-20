package com.on_bapsang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DishDto {
    @JsonProperty("recipe_id")
    private String recipeId;

    private String name;

    private List<String> ingredients;

    private String descriptions;

    private String review;

    private String time;

    private String difficulty;

    private String portion;

    private String method;

    @JsonProperty("material_type")
    private String materialType;

    @JsonProperty("image_url")
    private String imageUrl;

    private double score;
}
