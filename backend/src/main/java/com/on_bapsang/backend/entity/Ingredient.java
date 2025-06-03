package com.on_bapsang.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // 예: 돼지고기, 당근, 양파 등

    @Column(length = 30)
    private String type; // 예: '육류', '채소류' 등 (선택사항)
}
