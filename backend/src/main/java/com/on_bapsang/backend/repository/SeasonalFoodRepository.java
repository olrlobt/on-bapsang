package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.SeasonalFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeasonalFoodRepository extends JpaRepository<SeasonalFood, Long> {
    List<SeasonalFood> findBymDistctns(String mDistctns);
}
