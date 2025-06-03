package com.on_bapsang.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.on_bapsang.backend.dto.IngredientMarketMappingDto;
import com.on_bapsang.backend.dto.MarketRegionPriceResponse;
import com.on_bapsang.backend.dto.MarketTimeseriesResponse;
import com.on_bapsang.backend.repository.IngredientMasterRepository;
import com.on_bapsang.backend.repository.MarketPriceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientMarketMappingService {

    private final IngredientMasterRepository ingredientMasterRepository;

    private Map<Long, Integer> ingredientToMarketItemMap = new HashMap<>();
    private Map<Long, String> ingredientIdToNameMap = new HashMap<>();

    @PostConstruct
    public void loadMapping() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<IngredientMarketMappingDto>> typeRef = new TypeReference<>() {};

        List<IngredientMarketMappingDto> list = mapper.readValue(
                new ClassPathResource("ingredient_market_mapping.json").getInputStream(),
                typeRef
        );

        for (IngredientMarketMappingDto dto : list) {
            ingredientToMarketItemMap.put(dto.getIngredient_id(), dto.getMarket_item_id());
            ingredientIdToNameMap.put(dto.getIngredient_id(), dto.getIngredient_name());
        }
    }

    public Integer getMarketItemId(Long ingredientId) {
        return ingredientToMarketItemMap.get(ingredientId);
    }

    public String getIngredientName(Long ingredientId) {
        // 1️⃣ 먼저 json → map 조회 시도
        String name = ingredientIdToNameMap.get(ingredientId);
        if (name != null) {
            return name;
        }

        // 2️⃣ fallback: IngredientMaster 에서 조회
        return ingredientMasterRepository.findNameByIngredientId(ingredientId)
                .orElse("Unknown Ingredient (" + ingredientId + ")");
    }
}
