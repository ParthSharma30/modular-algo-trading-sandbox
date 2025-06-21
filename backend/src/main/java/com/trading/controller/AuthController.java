package com.trading.controller;

import com.trading.dto.LoginRequest;
import com.trading.dto.LoginResponse;
import com.trading.dto.UserDto;
import com.trading.service.AuthenticationService;
import com.trading.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto, 
                                          @RequestParam String password) {
        UserDto createdUser = userService.createUser(userDto, password);
        return ResponseEntity.ok(createdUser);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Refresh JWT token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        String newToken = authenticationService.refreshToken(refreshToken);
        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user details")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 