package com.trading.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data")
@EntityListeners(AuditingEntityListener.class)
public class MarketData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false, length = 20)
    private String symbol;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "open_price", precision = 10, scale = 4)
    private BigDecimal openPrice;
    
    @Column(name = "high_price", precision = 10, scale = 4)
    private BigDecimal highPrice;
    
    @Column(name = "low_price", precision = 10, scale = 4)
    private BigDecimal lowPrice;
    
    @Column(name = "close_price", precision = 10, scale = 4)
    private BigDecimal closePrice;
    
    private Long volume;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public MarketData() {}
    
    public MarketData(String symbol, LocalDateTime timestamp, BigDecimal closePrice) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.closePrice = closePrice;
    }
    
    public MarketData(String symbol, LocalDateTime timestamp, BigDecimal openPrice, 
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