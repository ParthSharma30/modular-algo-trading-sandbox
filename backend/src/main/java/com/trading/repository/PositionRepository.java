package com.trading.repository;

import com.trading.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    
    List<Position> findByPortfolioId(Long portfolioId);
    
    Optional<Position> findByPortfolioIdAndSymbol(Long portfolioId, String symbol);
    
    @Query("SELECT p FROM Position p WHERE p.portfolio.id = :portfolioId AND p.quantity > 0")
    List<Position> findOpenPositionsByPortfolioId(@Param("portfolioId") Long portfolioId);
    
    @Query("SELECT p FROM Position p WHERE p.portfolio.id = :portfolioId AND p.symbol = :symbol AND p.quantity > 0")
    Optional<Position> findOpenPositionByPortfolioIdAndSymbol(@Param("portfolioId") Long portfolioId, 
                                                             @Param("symbol") String symbol);
    
    boolean existsByPortfolioIdAndSymbol(Long portfolioId, String symbol);
} 