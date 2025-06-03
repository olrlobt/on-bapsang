package com.on_bapsang.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MarketRegionPriceResponse {

    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private String yearMonth;
    private List<MarketRegionDto> markets;

    @Getter
    @Setter
    @Builder
    public static class MarketRegionDto {
        private String market;
        private Integer averagePrice;
    }
}
