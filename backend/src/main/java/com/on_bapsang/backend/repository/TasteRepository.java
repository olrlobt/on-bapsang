package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasteRepository extends JpaRepository<Taste, Long> {
}
