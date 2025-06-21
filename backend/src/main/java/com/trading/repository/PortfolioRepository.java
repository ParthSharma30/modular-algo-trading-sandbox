package com.trading.repository;

import com.trading.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(Long userId);
    
    List<Portfolio> findByUserIdAndStatus(Long userId, String status);
    
    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId AND p.status = 'ACTIVE' ORDER BY p.createdAt DESC")
    List<Portfolio> findActivePortfoliosByUserId(@Param("userId") Long userId);
    
    Optional<Portfolio> findByIdAndUserId(Long id, Long userId);
    
    boolean existsByNameAndUserId(String name, Long userId);
} 