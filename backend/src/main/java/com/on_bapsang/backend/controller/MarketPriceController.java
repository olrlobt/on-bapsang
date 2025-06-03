package com.on_bapsang.backend.controller;

import com.on_bapsang.backend.dto.MarketRegionPriceResponse;
import com.on_bapsang.backend.dto.MarketTimeseriesResponse;
import com.on_bapsang.backend.service.MarketPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/market/price")
@RequiredArgsConstructor
public class MarketPriceController {

    private final MarketPriceService marketPriceService;

    @GetMapping("/region/{ingredientId}")
    public ResponseEntity<?> getRegionPrices(@PathVariable Long ingredientId,
                                             @RequestParam String yearMonth) {
        MarketRegionPriceResponse response = marketPriceService.getRegionPrices(ingredientId, yearMonth);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/timeseries/{ingredientId}")
    public ResponseEntity<?> getTimeseries(@PathVariable Long ingredientId) {
        MarketTimeseriesResponse response = marketPriceService.getTimeseries(ingredientId);
        return ResponseEntity.ok(response);
    }
}
