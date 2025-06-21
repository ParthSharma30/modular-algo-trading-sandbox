package com.trading.repository;

import com.trading.model.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {
    
    List<Strategy> findByUserIdAndIsActiveTrue(Long userId);
    
    List<Strategy> findByUserId(Long userId);
    
    @Query("SELECT s FROM Strategy s WHERE s.user.id = :userId AND s.isActive = true ORDER BY s.createdAt DESC")
    List<Strategy> findActiveStrategiesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT s FROM Strategy s WHERE s.strategyType = :strategyType AND s.isActive = true")
    List<Strategy> findByStrategyType(@Param("strategyType") String strategyType);
    
    boolean existsByNameAndUserId(String name, Long userId);
} 