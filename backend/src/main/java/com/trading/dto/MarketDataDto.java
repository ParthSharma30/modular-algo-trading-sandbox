package com.trading.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MarketDataDto {
    
    private Long id;
    
    @NotBlank
    private String symbol;
    
    @NotNull
    private LocalDateTime timestamp;
    
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long volume;
    private LocalDateTime createdAt;
    
    // Constructors
    public MarketDataDto() {}
    
    public MarketDataDto(String symbol, LocalDateTime timestamp, BigDecimal closePrice) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.closePrice = closePrice;
    }
    
    public MarketDataDto(String symbol, LocalDateTime timestamp, BigDecimal openPrice, 
                        BigDecimal highPrice, BigDecimal lowPrice, BigDecimal closePrice, Long volume) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public BigDecimal getOpenPrice() {
        return openPrice;
    }
    
    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }
    
    public BigDecimal getHighPrice() {
        return highPrice;
    }
    
    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }
    
    public BigDecimal getLowPrice() {
        return lowPrice;
    }
    
    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }
    
    public BigDecimal getClosePrice() {
        return closePrice;
    }
    
    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }
    
    public Long getVolume() {
        return volume;
    }
    
    public void setVolume(Long volume) {
        this.volume = volume;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 