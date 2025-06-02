package com.on_bapsang.backend.init;

import com.on_bapsang.backend.service.SeasonalFoodService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeasonalFoodInitializer {

    private final SeasonalFoodService foodService;

    @PostConstruct
    public void init() {
        foodService.fetchAndSaveAllMonths();
        System.out.println("제철 식재료 1~12월 데이터 자동 저장 완료");
    }
}
