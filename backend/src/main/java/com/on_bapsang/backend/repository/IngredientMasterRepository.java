package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.IngredientMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientMasterRepository extends JpaRepository<IngredientMaster, Long> {
    Optional<IngredientMaster> findByName(String name);
}