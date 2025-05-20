package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.RecommendRequest;
import com.on_bapsang.backend.dto.RecommendResponse;
import com.on_bapsang.backend.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecommendationService recommendationService;

    public RecipeController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/foreign")
    public ResponseEntity<RecommendResponse> recommend(
            @Valid @RequestBody RecommendRequest request
    ) {
        RecommendResponse resp = recommendationService.getRecommendations(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
