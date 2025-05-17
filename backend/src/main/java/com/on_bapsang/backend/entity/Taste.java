package com.on_bapsang.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Taste")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Taste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tasteId;

    @Column(nullable = false, unique = true, length = 30)
    private String name;  // 예: 매운맛, 짠맛, 단맛 등
}
