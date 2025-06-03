package com.on_bapsang.backend.repository;

import com.on_bapsang.backend.entity.MarketPrice;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

    @Query("SELECT DISTINCT m.marketName FROM MarketPrice m WHERE m.marketItemId = :marketItemId")
    List<String> findDistinctMarkets(@Param("marketItemId") Integer marketItemId);

    @Query("""
        SELECT 
            SUBSTRING(m.priceDate, 1, 6) as ym, 
            m.marketName, 
            AVG(m.price)
        FROM MarketPrice m 
        WHERE m.marketItemId = :marketItemId 
        AND SUBSTRING(m.priceDate, 1, 6) = :yearMonth
        GROUP BY SUBSTRING(m.priceDate, 1, 6), m.marketName
        ORDER BY m.marketName ASC
    """)
    List<Object[]> findMarketPricesByRegion(@Param("marketItemId") Integer marketItemId,
                                            @Param("yearMonth") String yearMonth);

    @Query("SELECT SUBSTRING(m.priceDate, 1, 6) as ym, AVG(m.price) " +
            "FROM MarketPrice m " +
            "WHERE m.marketItemId = :marketItemId AND m.marketName = '경동' " +
            "GROUP BY ym ORDER BY ym ASC")
    List<Object[]> findTimeseriesForMarket(@Param("marketItemId") Integer marketItemId);
}
