package com.on_bapsang.backend.service;

import com.on_bapsang.backend.dto.MarketRegionPriceResponse;
import com.on_bapsang.backend.dto.MarketTimeseriesResponse;
import com.on_bapsang.backend.entity.MarketItem;
import com.on_bapsang.backend.repository.MarketItemRepository;
import com.on_bapsang.backend.repository.MarketPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketPriceService {

    private final MarketPriceRepository marketPriceRepository;
    private final MarketItemRepository marketItemRepository;
    private final IngredientMarketMappingService mappingService;

    public MarketRegionPriceResponse getRegionPrices(Long ingredientId, String yearMonth) {
        Integer marketItemId = mappingService.getMarketItemId(ingredientId);
        if (marketItemId == null) {
            throw new IllegalArgumentException("No market data for ingredient_id=" + ingredientId);
        }

        List<Object[]> results = marketPriceRepository.findMarketPricesByRegion(marketItemId, yearMonth);

        List<MarketRegionPriceResponse.MarketRegionDto> markets = results.isEmpty() ? List.of() :
                results.stream()
                        .map(row -> MarketRegionPriceResponse.MarketRegionDto.builder()
                                .market((String) row[1])
                                .averagePrice(((Number) row[2]).intValue())
                                .build())
                        .toList();

        MarketItem marketItem = marketItemRepository.findById(marketItemId)
                .orElseThrow(() -> new IllegalArgumentException("MarketItem not found"));

        String ingredientName = mappingService.getIngredientName(ingredientId);
        if (ingredientName == null || ingredientName.equals("Unknown Ingredient")) {
            ingredientName = "Unknown Ingredient (" + ingredientId + ")";
        }

        return MarketRegionPriceResponse.builder()
                .ingredientId(ingredientId)
                .ingredientName(ingredientName)
                .unit(marketItem.getSpcsName())
                .yearMonth(yearMonth)
                .markets(markets)
                .build();
    }

    public MarketTimeseriesResponse getTimeseries(Long ingredientId) {
        Integer marketItemId = mappingService.getMarketItemId(ingredientId);
        if (marketItemId == null) {
            throw new IllegalArgumentException("No market data for ingredient_id=" + ingredientId);
        }

        List<Object[]> results = marketPriceRepository.findTimeseriesForMarket(marketItemId);

        List<MarketTimeseriesResponse.MonthlyPriceDto> prices = results.isEmpty() ? List.of() :
                results.stream()
                        .map(row -> MarketTimeseriesResponse.MonthlyPriceDto.builder()
                                .date((String) row[0])
                                .price(((Number) row[1]).intValue())
                                .build())
                        .toList();

        MarketItem marketItem = marketItemRepository.findById(marketItemId)
                .orElseThrow(() -> new IllegalArgumentException("MarketItem not found"));

        String ingredientName = mappingService.getIngredientName(ingredientId);
        if (ingredientName == null || ingredientName.equals("Unknown Ingredient")) {
            ingredientName = "Unknown Ingredient (" + ingredientId + ")";
        }

        return MarketTimeseriesResponse.builder()
                .ingredient(ingredientName)
                .market("경동")
                .unit(marketItem.getSpcsName())
                .origin(marketItem.getDetail() != null ? marketItem.getDetail() : "정보없음")
                .monthlyPrices(prices)
                .build();
    }
}
