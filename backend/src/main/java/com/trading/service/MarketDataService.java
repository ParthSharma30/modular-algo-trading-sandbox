package com.trading.service;

import com.trading.dto.MarketDataDto;
import com.trading.model.MarketData;
import com.trading.repository.MarketDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MarketDataService {
    
    @Autowired
    private MarketDataRepository marketDataRepository;
    
    @Value("${trading.market-data.api-key}")
    private String apiKey;
    
    private final Random random = new Random();
    
    public List<MarketDataDto> getAllMarketData() {
        return marketDataRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<MarketDataDto> getMarketDataBySymbol(String symbol) {
        return marketDataRepository.findBySymbolOrderByTimestampDesc(symbol).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Cacheable(value = "marketData", key = "#symbol")
    public Optional<MarketDataDto> getLatestMarketData(String symbol) {
        return marketDataRepository.findLatestBySymbol(symbol)
                .map(this::convertToDto);
    }
    
    public List<MarketDataDto> getHistoricalData(String symbol, LocalDateTime startDate, LocalDateTime endDate) {
        return marketDataRepository.findBySymbolAndDateRange(symbol, startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public MarketDataDto createMarketData(MarketDataDto marketDataDto) {
        MarketData marketData = new MarketData();
        marketData.setSymbol(marketDataDto.getSymbol());
        marketData.setTimestamp(marketDataDto.getTimestamp());
        marketData.setOpenPrice(marketDataDto.getOpenPrice());
        marketData.setHighPrice(marketDataDto.getHighPrice());
        marketData.setLowPrice(marketDataDto.getLowPrice());
        marketData.setClosePrice(marketDataDto.getClosePrice());
        marketData.setVolume(marketDataDto.getVolume());
        
        MarketData savedMarketData = marketDataRepository.save(marketData);
        return convertToDto(savedMarketData);
    }
    
    public void deleteOldMarketData(String symbol, LocalDateTime beforeDate) {
        marketDataRepository.deleteBySymbolAndTimestampBefore(symbol, beforeDate);
    }
    
    // Simulated market data generation
    public MarketDataDto generateSimulatedData(String symbol) {
        // Generate realistic price movements
        BigDecimal basePrice = new BigDecimal("100.00");
        BigDecimal volatility = new BigDecimal("0.02"); // 2% volatility
        
        BigDecimal priceChange = basePrice.multiply(volatility)
                .multiply(BigDecimal.valueOf(random.nextDouble() - 0.5))
                .setScale(4, RoundingMode.HALF_UP);
        
        BigDecimal closePrice = basePrice.add(priceChange);
        BigDecimal openPrice = basePrice;
        BigDecimal highPrice = closePrice.max(openPrice).add(BigDecimal.valueOf(random.nextDouble() * 2));
        BigDecimal lowPrice = closePrice.min(openPrice).subtract(BigDecimal.valueOf(random.nextDouble() * 2));
        Long volume = random.nextLong(1000000) + 100000;
        
        MarketDataDto marketData = new MarketDataDto(
                symbol, LocalDateTime.now(), openPrice, highPrice, lowPrice, closePrice, volume
        );
        
        return createMarketData(marketData);
    }
    
    // Scheduled task to generate simulated market data
    @Scheduled(fixedRate = 60000) // Every 60 seconds
    public void generateSimulatedMarketData() {
        String[] symbols = {"AAPL", "GOOGL", "MSFT", "TSLA", "AMZN"};
        System.out.println("[MarketDataService] Generating simulated market data for symbols: " + String.join(", ", symbols));
        for (String symbol : symbols) {
            generateSimulatedData(symbol);
        }
    }
    
    // Fetch real market data from external API (placeholder)
    public MarketDataDto fetchRealMarketData(String symbol) {
        // This would integrate with a real market data provider like Alpha Vantage
        // For now, return simulated data
        return generateSimulatedData(symbol);
    }
    
    public void startSimulation(String symbol, double speedMultiplier) {
        // Start real-time simulation for a symbol
        // This would involve:
        // 1. Loading historical data
        // 2. Playing it back at the specified speed
        // 3. Broadcasting updates via WebSocket
        
        System.out.println("Starting simulation for " + symbol + " at " + speedMultiplier + "x speed");
    }
    
    private MarketDataDto convertToDto(MarketData marketData) {
        MarketDataDto dto = new MarketDataDto();
        dto.setId(marketData.getId());
        dto.setSymbol(marketData.getSymbol());
        dto.setTimestamp(marketData.getTimestamp());
        dto.setOpenPrice(marketData.getOpenPrice());
        dto.setHighPrice(marketData.getHighPrice());
        dto.setLowPrice(marketData.getLowPrice());
        dto.setClosePrice(marketData.getClosePrice());
        dto.setVolume(marketData.getVolume());
        dto.setCreatedAt(marketData.getCreatedAt());
        return dto;
    }
} 