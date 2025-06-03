package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketItemRepository extends JpaRepository<MarketItem, Integer> {
}
