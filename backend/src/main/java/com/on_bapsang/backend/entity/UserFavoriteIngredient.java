package com.on_bapsang.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserFavoriteIngredient")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFavoriteIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(length = 10)
    private String preferenceType; // 예: '좋아함' 또는 '싫어함' 등
}
