package com.trading.repository;

import com.trading.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    
    List<Trade> findByPortfolioId(Long portfolioId);
    
    List<Trade> findByPortfolioIdOrderByCreatedAtDesc(Long portfolioId);
    
    List<Trade> findByPortfolioIdAndStatus(Long portfolioId, String status);
    
    List<Trade> findBySymbol(String symbol);
    
    @Query("SELECT t FROM Trade t WHERE t.portfolio.id = :portfolioId AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Trade> findByPortfolioIdAndDateRange(@Param("portfolioId") Long portfolioId, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Trade t WHERE t.portfolio.id = :portfolioId AND t.status = 'FILLED' ORDER BY t.executedAt DESC")
    List<Trade> findFilledTradesByPortfolioId(@Param("portfolioId") Long portfolioId);
} 