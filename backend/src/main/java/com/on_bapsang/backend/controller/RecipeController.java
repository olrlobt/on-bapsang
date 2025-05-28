package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.RecommendRequest;
import com.on_bapsang.backend.dto.RecommendResponse;
import com.on_bapsang.backend.service.RecommendationService;
import com.on_bapsang.backend.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.on_bapsang.backend.dto.RecipeDetailDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecommendationService recommendationService;
    private final RecipeService recipeService;

    @PostMapping("/foreign")
    public ResponseEntity<RecommendResponse> recommend(
            @Valid @RequestBody RecommendRequest request
    ) {
        RecommendResponse resp = recommendationService.getRecommendations(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/foreign/{recipeId}")
    public ResponseEntity<RecipeDetailDto> getRecipeDetail(
            @PathVariable String recipeId
    ) {
        RecipeDetailDto detail = recipeService.getRecipeDetail(recipeId);
        return ResponseEntity.ok(detail);
    }
}
