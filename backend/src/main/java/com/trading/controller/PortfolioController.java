package com.trading.controller;

import com.trading.dto.PortfolioDto;
import com.trading.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/portfolios")
@Tag(name = "Portfolios", description = "Portfolio management APIs")
@CrossOrigin(origins = "*")
public class PortfolioController {
    
    @Autowired
    private PortfolioService portfolioService;
    
    @GetMapping
    @Operation(summary = "Get all portfolios", description = "Get all portfolios for the current user")
    public ResponseEntity<List<PortfolioDto>> getAllPortfolios() {
        Long userId = getCurrentUserId();
        List<PortfolioDto> portfolios = portfolioService.getPortfoliosByUserId(userId);
        return ResponseEntity.ok(portfolios);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get portfolio by ID", description = "Get a specific portfolio by its ID")
    public ResponseEntity<PortfolioDto> getPortfolioById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return portfolioService.getPortfolioByIdAndUserId(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create portfolio", description = "Create a new portfolio")
    public ResponseEntity<PortfolioDto> createPortfolio(@Valid @RequestBody PortfolioDto portfolioDto) {
        Long userId = getCurrentUserId();
        PortfolioDto createdPortfolio = portfolioService.createPortfolio(portfolioDto, userId);
        return ResponseEntity.ok(createdPortfolio);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update portfolio", description = "Update an existing portfolio")
    public ResponseEntity<PortfolioDto> updatePortfolio(@PathVariable Long id, 
                                                       @Valid @RequestBody PortfolioDto portfolioDto) {
        Long userId = getCurrentUserId();
        PortfolioDto updatedPortfolio = portfolioService.updatePortfolio(id, portfolioDto, userId);
        return ResponseEntity.ok(updatedPortfolio);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete portfolio", description = "Delete a portfolio")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        portfolioService.deletePortfolio(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/value")
    @Operation(summary = "Get portfolio value", description = "Get current portfolio value")
    public ResponseEntity<BigDecimal> getPortfolioValue(@PathVariable Long id) {
        BigDecimal value = portfolioService.calculatePortfolioValue(id);
        return ResponseEntity.ok(value);
    }
    
    private Long getCurrentUserId() {
        // Always return the admin user's ID for demo purposes
        return 1L;
    }
} 