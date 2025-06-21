package com.trading.service;

import com.trading.dto.PortfolioDto;
import com.trading.model.Portfolio;
import com.trading.model.User;
import com.trading.repository.PortfolioRepository;
import com.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<PortfolioDto> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<PortfolioDto> getPortfoliosByUserId(Long userId) {
        return portfolioRepository.findActivePortfoliosByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<PortfolioDto> getPortfolioById(Long id) {
        return portfolioRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public Optional<PortfolioDto> getPortfolioByIdAndUserId(Long id, Long userId) {
        return portfolioRepository.findByIdAndUserId(id, userId)
                .map(this::convertToDto);
    }
    
    public PortfolioDto createPortfolio(PortfolioDto portfolioDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (portfolioRepository.existsByNameAndUserId(portfolioDto.getName(), userId)) {
            throw new RuntimeException("Portfolio name already exists for this user");
        }
        
        Portfolio portfolio = new Portfolio();
        portfolio.setName(portfolioDto.getName());
        portfolio.setInitialCapital(portfolioDto.getInitialCapital());
        portfolio.setCurrentCapital(portfolioDto.getInitialCapital());
        portfolio.setTotalPnl(BigDecimal.ZERO);
        portfolio.setStatus("ACTIVE");
        portfolio.setUser(user);
        
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return convertToDto(savedPortfolio);
    }
    
    public PortfolioDto updatePortfolio(Long id, PortfolioDto portfolioDto, Long userId) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        if (!portfolio.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to portfolio");
        }
        
        portfolio.setName(portfolioDto.getName());
        portfolio.setStatus(portfolioDto.getStatus());
        
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return convertToDto(savedPortfolio);
    }
    
    public void deletePortfolio(Long id, Long userId) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        if (!portfolio.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to portfolio");
        }
        
        portfolio.setStatus("CLOSED");
        portfolioRepository.save(portfolio);
    }
    
    public void updatePortfolioCapital(Long portfolioId, BigDecimal newCapital) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        portfolio.setCurrentCapital(newCapital);
        portfolio.setTotalPnl(newCapital.subtract(portfolio.getInitialCapital()));
        portfolioRepository.save(portfolio);
    }
    
    public BigDecimal calculatePortfolioValue(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        
        // This would include:
        // 1. Current cash balance
        // 2. Market value of all positions
        // 3. Unrealized P&L
        
        return portfolio.getCurrentCapital();
    }
    
    private PortfolioDto convertToDto(Portfolio portfolio) {
        PortfolioDto dto = new PortfolioDto();
        dto.setId(portfolio.getId());
        dto.setName(portfolio.getName());
        dto.setInitialCapital(portfolio.getInitialCapital());
        dto.setCurrentCapital(portfolio.getCurrentCapital());
        dto.setTotalPnl(portfolio.getTotalPnl());
        dto.setStatus(portfolio.getStatus());
        dto.setUserId(portfolio.getUser().getId());
        dto.setCreatedAt(portfolio.getCreatedAt());
        dto.setUpdatedAt(portfolio.getUpdatedAt());
        return dto;
    }
} 