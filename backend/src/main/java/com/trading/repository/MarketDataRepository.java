package com.trading.repository;

import com.trading.model.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
    
    List<MarketData> findBySymbolOrderByTimestampDesc(String symbol);
    
    List<MarketData> findBySymbolAndTimestampBetweenOrderByTimestampAsc(String symbol, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT md FROM MarketData md WHERE md.symbol = :symbol ORDER BY md.timestamp DESC LIMIT 1")
    Optional<MarketData> findLatestBySymbol(@Param("symbol") String symbol);
    
    @Query("SELECT md FROM MarketData md WHERE md.symbol = :symbol AND md.timestamp >= :startDate ORDER BY md.timestamp ASC")
    List<MarketData> findBySymbolAndTimestampAfter(@Param("symbol") String symbol, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT md FROM MarketData md WHERE md.symbol = :symbol AND md.timestamp BETWEEN :startDate AND :endDate ORDER BY md.timestamp ASC")
    List<MarketData> findBySymbolAndDateRange(@Param("symbol") String symbol, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    void deleteBySymbolAndTimestampBefore(String symbol, LocalDateTime timestamp);
} 