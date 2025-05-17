package com.on_bapsang.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignupRequest {

    // 기본 회원 정보
    private String username;
    private String password;
    private String nickname;
    private String country;
    private Integer age;      // 10, 20, 30 등
    private String location;  // 지역

    // 선호 정보 (ID 목록으로 받음)
    private List<Long> favoriteTasteIds;       // 선택
    private List<Long> favoriteDishIds;        // 선택
    private List<Long> favoriteIngredientIds;  // 필수
}
