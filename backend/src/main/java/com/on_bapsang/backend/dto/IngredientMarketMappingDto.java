package com.on_bapsang.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientMarketMappingDto {

    private Long ingredient_id;
    private Integer market_item_id;
    private String ingredient_name;
}
