package com.trading.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
@EntityListeners(AuditingEntityListener.class)
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotNull
    @Column(name = "initial_capital", nullable = false, precision = 15, scale = 2)
    private BigDecimal initialCapital;
    
    @NotNull
    @Column(name = "current_capital", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentCapital;
    
    @Column(name = "total_pnl", precision = 15, scale = 2)
    private BigDecimal totalPnl = BigDecimal.ZERO;
    
    @Column(length = 20)
    private String status = "ACTIVE";
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trade> trades = new ArrayList<>();
    
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Position> positions = new ArrayList<>();
    
    // Constructors
    public Portfolio() {}
    
    public Portfolio(String name, BigDecimal initialCapital, User user) {
        this.name = name;
        this.initialCapital = initialCapital;
        this.currentCapital = initialCapital;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public List<Trade> getTrades() {
        return trades;
    }
    
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
    
    public List<Position> getPositions() {
        return positions;
    }
    
    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
} 