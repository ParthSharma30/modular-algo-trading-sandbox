package com.trading.dto;

import java.time.LocalDateTime;

public class LoginResponse {
    
    private String token;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private UserDto user;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(String token, String refreshToken, LocalDateTime expiresAt, UserDto user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.user = user;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public UserDto getUser() {
        return user;
    }
    
    public void setUser(UserDto user) {
        this.user = user;
    }
} 