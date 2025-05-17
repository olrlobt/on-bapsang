package com.on_bapsang.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private String nickname;
    private Integer age;
    private List<Long> favoriteTasteIds;
    private List<Long> favoriteDishIds;
    private List<Long> favoriteIngredientIds;
}
