package com.on_bapsang.backend.service;
import com.on_bapsang.backend.dto.IngredientDetailDto;
import com.on_bapsang.backend.dto.RecipeDetailDto;
import com.on_bapsang.backend.entity.IngredientMaster;
import com.on_bapsang.backend.entity.Recipe;
import com.on_bapsang.backend.entity.RecipeIngredient;
import com.on_bapsang.backend.exception.CustomException;
import com.on_bapsang.backend.repository.RecipeIngredientRepository;
import com.on_bapsang.backend.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeDetailDto getRecipeDetail(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new CustomException("레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // ① 재료 매핑 (변경 없음)
        List<IngredientDetailDto> ingredients = recipeIngredientRepository
                .findByRecipeId(recipeId)
                .stream()
                .map(rel -> {
                    IngredientMaster im = rel.getIngredientMaster();
                    return new IngredientDetailDto(
                            im.getIngredientId().toString(),
                            im.getName(),
                            rel.getAmount()
                    );
                })
                .collect(Collectors.toList());

        // ② instruction 칼럼(텍스트)을 numbered list 기준으로 분리
        String raw = recipe.getInstruction();  // 전체 텍스트
        List<String> steps = Collections.emptyList();
        if (raw != null && !raw.isBlank()) {
            steps = Arrays.stream(raw.split("\\r?\\n"))
                    .map(String::trim)
                    .filter(s -> s.matches("^\\d+\\..+"))  // “1. …” 같은 라인만
                    .map(s -> s.replaceFirst("^\\d+\\.\\s*", ""))  // “1. ” 제거
                    .collect(Collectors.toList());
        }

        // ③ DTO 조립
        return new RecipeDetailDto(
                recipe.getRecipeId(),
                recipe.getName(),
                recipe.getImageUrl(),
                recipe.getTime(),
                recipe.getDifficulty(),
                recipe.getPortion(),
                recipe.getMethod(),
                recipe.getMaterialType(),
                ingredients,
                steps,                          // 분리된 단계 리스트
                recipe.getReview(),
                recipe.getDescription()         // 엔티티 필드명에 맞춰 getDescription()
        );
    }
}