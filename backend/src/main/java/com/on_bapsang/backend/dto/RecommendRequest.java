package com.on_bapsang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RecommendRequest {
    @JsonProperty("food_name")
    @NotBlank(message = "음식 이름을 입력해 주세요.")
    @Size(max = 20, message = "음식 이름은 최대 20자까지 가능합니다.")
    @Pattern(
            regexp = "^[가-힣A-Za-z0-9 ]+$",
            message = "특수문자는 사용할 수 없습니다."
    )
    private String foodName;
}