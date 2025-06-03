package com.on_bapsang.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MarketTimeseriesResponse {

    private String ingredient;
    private String market;
    private String unit;
    private String origin;
    private List<MonthlyPriceDto> monthlyPrices;

    @Getter
    @Setter
    @Builder
    public static class MonthlyPriceDto {
        private String date;
        private Integer price;
    }
}
