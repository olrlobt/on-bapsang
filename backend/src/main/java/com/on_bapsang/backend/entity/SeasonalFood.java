package com.on_bapsang.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seasonal_food")
@Getter
@Setter
@NoArgsConstructor
public class SeasonalFood {

    @Id
    private Long idntfcNo; // 식품 고유 번호 (PK)

    private String prdlstNm;      // 품목명
    private String mDistctns;     // 제철 월
    @Column(length = 2000)
    private String effect;        // 효능
    @Column(length = 2000)
    private String purchaseMth;   // 구입요령
    @Column(length = 2000)
    private String cookMth;       // 조리법
    private String imgUrl;        // 이미지 URL
}
