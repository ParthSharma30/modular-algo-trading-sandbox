package com.trading.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PortfolioDto {
    
    private Long id;
    
    @NotBlank
    private String name;
    
    @NotNull
    @Positive
    private BigDecimal initialCapital;
    
    private BigDecimal currentCapital;
    private BigDecimal totalPnl;
    private String status;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public PortfolioDto() {}
    
    public PortfolioDto(String name, BigDecimal initialCapital) {
        this.name = name;
        this.initialCapital = initialCapital;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getInitialCapital() {
        return initialCapital;
    }
    
    public void setInitialCapital(BigDecimal initialCapital) {
        this.initialCapital = initialCapital;
    }
    
    public BigDecimal getCurrentCapital() {
        return currentCapital;
    }
    
    public void setCurrentCapital(BigDecimal currentCapital) {
        this.currentCapital = currentCapital;
    }
    
    public BigDecimal getTotalPnl() {
        return totalPnl;
    }
    
    public void setTotalPnl(BigDecimal totalPnl) {
        this.totalPnl = totalPnl;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 