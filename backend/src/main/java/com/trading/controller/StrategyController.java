package com.trading.controller;

import com.trading.dto.StrategyDto;
import com.trading.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/strategies")
@Tag(name = "Strategies", description = "Trading strategy management APIs")
@CrossOrigin(origins = "*")
public class StrategyController {
    
    @Autowired
    private StrategyService strategyService;
    
    @GetMapping
    @Operation(summary = "Get all strategies", description = "Get all strategies for the current user")
    public ResponseEntity<List<StrategyDto>> getAllStrategies() {
        Long userId = getCurrentUserId();
        List<StrategyDto> strategies = strategyService.getStrategiesByUserId(userId);
        return ResponseEntity.ok(strategies);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get strategy by ID", description = "Get a specific strategy by its ID")
    public ResponseEntity<StrategyDto> getStrategyById(@PathVariable Long id) {
        return strategyService.getStrategyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create strategy", description = "Create a new trading strategy")
    public ResponseEntity<StrategyDto> createStrategy(@Valid @RequestBody StrategyDto strategyDto) {
        Long userId = getCurrentUserId();
        StrategyDto createdStrategy = strategyService.createStrategy(strategyDto, userId);
        return ResponseEntity.ok(createdStrategy);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update strategy", description = "Update an existing trading strategy")
    public ResponseEntity<StrategyDto> updateStrategy(@PathVariable Long id, 
                                                    @Valid @RequestBody StrategyDto strategyDto) {
        Long userId = getCurrentUserId();
        StrategyDto updatedStrategy = strategyService.updateStrategy(id, strategyDto, userId);
        return ResponseEntity.ok(updatedStrategy);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete strategy", description = "Delete a trading strategy")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        strategyService.deleteStrategy(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/execute")
    @Operation(summary = "Execute strategy", description = "Execute a trading strategy")
    public ResponseEntity<Void> executeStrategy(@PathVariable Long id, 
                                              @RequestParam Long portfolioId) {
        strategyService.executeStrategy(id, portfolioId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/backtest")
    @Operation(summary = "Backtest strategy", description = "Run backtesting on a strategy")
    public ResponseEntity<Void> backtestStrategy(@PathVariable Long id,
                                               @RequestParam String startDate,
                                               @RequestParam String endDate) {
        strategyService.backtestStrategy(id, startDate, endDate);
        return ResponseEntity.ok().build();
    }
    
    private Long getCurrentUserId() {
        // Always return the admin user's ID for demo purposes
        return 1L;
    }
} 