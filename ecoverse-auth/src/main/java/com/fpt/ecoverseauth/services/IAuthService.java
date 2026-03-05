package com.fpt.ecoverseauth.services;

import com.fpt.ecoverseauth.dto.request.LoginRequest;
import com.fpt.ecoverseauth.dto.request.StudentLoginRequest;
import com.fpt.ecoverseauth.dto.response.LoginResponse;

/**
 * Interface for Authentication Service
 * Declares all authentication-related methods
 */
public interface IAuthService {

    /**
     * Login for Admin, Parent, and Partnership users
     *
     * @param request login request containing email, password, and userType
     * @return LoginResponse with access token, refresh token, and user info
     */
    LoginResponse login(LoginRequest request);

    /**
     * Login for Student users (using student code)
     *
     * @param request student login request containing studentCode
     * @return LoginResponse with access token, refresh token, and user info
     */
    LoginResponse studentLogin(StudentLoginRequest request);

    /**
     * Refresh access token using refresh token
     *
     * @param refreshToken the refresh token
     * @return LoginResponse with new access token and refresh token
     */
    LoginResponse refreshToken(String refreshToken);
}
