package com.trading.service;

import com.trading.dto.StrategyDto;
import com.trading.model.Strategy;
import com.trading.model.User;
import com.trading.repository.StrategyRepository;
import com.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class StrategyService {
    
    @Autowired
    private StrategyRepository strategyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public List<StrategyDto> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<StrategyDto> getStrategiesByUserId(Long userId) {
        return strategyRepository.findActiveStrategiesByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<StrategyDto> getStrategyById(Long id) {
        return strategyRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public StrategyDto createStrategy(StrategyDto strategyDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (strategyRepository.existsByNameAndUserId(strategyDto.getName(), userId)) {
            throw new RuntimeException("Strategy name already exists for this user");
        }
        
        Strategy strategy = new Strategy();
        strategy.setName(strategyDto.getName());
        strategy.setDescription(strategyDto.getDescription());
        strategy.setStrategyType(strategyDto.getStrategyType());

        Object params = strategyDto.getParameters();
        if (params instanceof String) {
            try {
                params = objectMapper.readValue((String) params, Map.class);
            } catch (Exception e) {
                params = new java.util.HashMap<>();
            }
        }
        strategy.setParameters(params != null ? (Map<String, Object>) params : new java.util.HashMap<>());

        Object rules = strategyDto.getRules();
        if (rules instanceof String) {
            try {
                rules = objectMapper.readValue((String) rules, Map.class);
            } catch (Exception e) {
                rules = new java.util.HashMap<>();
            }
        }
        strategy.setRules(rules != null ? (Map<String, Object>) rules : new java.util.HashMap<>());

        strategy.setUser(user);
        strategy.setIsActive(true);
        
        // Optional: Debug logging
        System.out.println("PARAMETERS: " + strategy.getParameters());
        System.out.println("RULES: " + strategy.getRules());

        Strategy savedStrategy = strategyRepository.save(strategy);
        return convertToDto(savedStrategy);
    }
    
    public StrategyDto updateStrategy(Long id, StrategyDto strategyDto, Long userId) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        
        if (!strategy.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to strategy");
        }
        
        strategy.setName(strategyDto.getName());
        strategy.setDescription(strategyDto.getDescription());
        strategy.setStrategyType(strategyDto.getStrategyType());
        strategy.setParameters(strategyDto.getParameters());
        strategy.setRules(strategyDto.getRules());
        
        Strategy savedStrategy = strategyRepository.save(strategy);
        return convertToDto(savedStrategy);
    }
    
    public void deleteStrategy(Long id, Long userId) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        
        if (!strategy.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to strategy");
        }
        
        strategy.setIsActive(false);
        strategyRepository.save(strategy);
    }
    
    public void executeStrategy(Long strategyId, Long portfolioId) {
        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        
        // Strategy execution logic would go here
        // This would involve:
        // 1. Getting current market data
        // 2. Evaluating strategy rules
        // 3. Generating orders
        // 4. Executing trades
        
        System.out.println("Executing strategy: " + strategy.getName());
    }
    
    public void backtestStrategy(Long strategyId, String startDate, String endDate) {
        Strategy strategy = strategyRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        
        // Backtesting logic would go here
        // This would involve:
        // 1. Loading historical data
        // 2. Simulating strategy execution
        // 3. Calculating performance metrics
        
        System.out.println("Backtesting strategy: " + strategy.getName());
    }
    
    private StrategyDto convertToDto(Strategy strategy) {
        StrategyDto dto = new StrategyDto();
        dto.setId(strategy.getId());
        dto.setName(strategy.getName());
        dto.setDescription(strategy.getDescription());
        dto.setStrategyType(strategy.getStrategyType());
        dto.setParameters(strategy.getParameters());
        dto.setRules(strategy.getRules());
        dto.setIsActive(strategy.getIsActive());
        dto.setUserId(strategy.getUser().getId());
        dto.setCreatedAt(strategy.getCreatedAt());
        dto.setUpdatedAt(strategy.getUpdatedAt());
        return dto;
    }
} 