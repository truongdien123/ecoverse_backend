package com.fpt.ecoverseauth.controllers;

import com.fpt.ecoverseauth.dto.request.LoginRequest;
import com.fpt.ecoverseauth.dto.request.StudentLoginRequest;
import com.fpt.ecoverseauth.dto.response.LoginResponse;
import com.fpt.ecoverseauth.services.IAuthService;
import com.fpt.ecoversecommon.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - handles login endpoints
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * Login endpoint for Admin, Parent, and Partnership users
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Login endpoint for Student users (using student code)
     * POST /api/auth/student/login
     */
    @PostMapping("/student/login")
    public ResponseEntity<ApiResponse<LoginResponse>> studentLogin(@Valid @RequestBody StudentLoginRequest request) {
        LoginResponse response = authService.studentLogin(request);
        return ResponseEntity.ok(ApiResponse.success("Student login successful", response));
    }

    /**
     * Refresh token endpoint
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@RequestParam String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    /**
     * Logout endpoint (client-side token removal)
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // JWT is stateless, so logout is handled on client side by removing the token
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }

    /**
     * Validate token endpoint
     * GET /api/auth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        // If this endpoint is reached, the token is valid (filtered by JwtAuthenticationFilter)
        return ResponseEntity.ok(ApiResponse.success("Token is valid", null));
    }
}
