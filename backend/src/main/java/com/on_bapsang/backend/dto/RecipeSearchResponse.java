package com.on_bapsang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class RecipeSearchResponse {
    private int count;
    private List<RecipeSummaryDto> recipes;
}
