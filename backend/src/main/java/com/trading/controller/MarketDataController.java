package com.trading.controller;

import com.trading.dto.MarketDataDto;
import com.trading.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/market-data")
@Tag(name = "Market Data", description = "Market data management APIs")
@CrossOrigin(origins = "*")
public class MarketDataController {
    
    @Autowired
    private MarketDataService marketDataService;
    
    @GetMapping
    @Operation(summary = "Get all market data", description = "Get all market data records")
    public ResponseEntity<List<MarketDataDto>> getAllMarketData() {
        List<MarketDataDto> marketData = marketDataService.getAllMarketData();
        return ResponseEntity.ok(marketData);
    }
    
    @GetMapping("/{symbol}")
    @Operation(summary = "Get market data by symbol", description = "Get market data for a specific symbol")
    public ResponseEntity<List<MarketDataDto>> getMarketDataBySymbol(@PathVariable String symbol) {
        List<MarketDataDto> marketData = marketDataService.getMarketDataBySymbol(symbol);
        return ResponseEntity.ok(marketData);
    }
    
    @GetMapping("/{symbol}/latest")
    @Operation(summary = "Get latest market data", description = "Get the latest market data for a symbol")
    public ResponseEntity<MarketDataDto> getLatestMarketData(@PathVariable String symbol) {
        Optional<MarketDataDto> marketData = marketDataService.getLatestMarketData(symbol);
        return marketData.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{symbol}/history")
    @Operation(summary = "Get historical market data", description = "Get historical market data for a symbol")
    public ResponseEntity<List<MarketDataDto>> getHistoricalData(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MarketDataDto> marketData = marketDataService.getHistoricalData(symbol, startDate, endDate);
        return ResponseEntity.ok(marketData);
    }
    
    @PostMapping("/{symbol}/simulate")
    @Operation(summary = "Start simulation", description = "Start market data simulation for a symbol")
    public ResponseEntity<Void> startSimulation(@PathVariable String symbol,
                                              @RequestParam(defaultValue = "1.0") double speedMultiplier) {
        marketDataService.startSimulation(symbol, speedMultiplier);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{symbol}/generate")
    @Operation(summary = "Generate simulated data", description = "Generate simulated market data for a symbol")
    public ResponseEntity<MarketDataDto> generateSimulatedData(@PathVariable String symbol) {
        MarketDataDto marketData = marketDataService.generateSimulatedData(symbol);
        return ResponseEntity.ok(marketData);
    }
    
    @PostMapping
    @Operation(summary = "Create market data", description = "Create a new market data record")
    public ResponseEntity<MarketDataDto> createMarketData(@RequestBody MarketDataDto marketDataDto) {
        MarketDataDto createdMarketData = marketDataService.createMarketData(marketDataDto);
        return ResponseEntity.ok(createdMarketData);
    }
} 