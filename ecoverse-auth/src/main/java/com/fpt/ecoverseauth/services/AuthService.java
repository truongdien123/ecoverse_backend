package com.fpt.ecoverseauth.services;

import com.fpt.ecoverseauth.dto.request.LoginRequest;
import com.fpt.ecoverseauth.dto.request.StudentLoginRequest;
import com.fpt.ecoverseauth.dto.response.LoginResponse;
import com.fpt.ecoverseauth.enums.UserType;
import com.fpt.ecoverseauth.security.CustomUserDetails;
import com.fpt.ecoverseauth.security.CustomUserDetailsService;
import com.fpt.ecoverseauth.utils.JwtUtils;
import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service - handles login logic for all user types
 */
@Service
public class AuthService implements IAuthService {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Login for Admin, Parent, and Partnership users
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // Validate user type
        if (request.getUserType() == UserType.STUDENT) {
            throw new BadRequestException("Students should use student code to login");
        }

        // Load user by email and type
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                .loadUserByEmailAndType(request.getEmail(), request.getUserType());

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Check if user is active
        if (!userDetails.isEnabled()) {
            throw new ForbiddenException("Account is disabled");
        }

        // Generate tokens
        String accessToken = jwtUtils.generateAccessToken(
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getUserType()
        );

        String refreshToken = jwtUtils.generateRefreshToken(
                userDetails.getId(),
                userDetails.getUserType()
        );

        // Build response
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationInSeconds())
                .userType(userDetails.getUserType())
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(userDetails.getId())
                        .fullName(userDetails.getFullName())
                        .email(userDetails.getEmail())
                        .avatarUrl(userDetails.getAvatarUrl())
                        .build())
                .build();
    }

    /**
     * Login for Student users (using student code)
     */
    @Override
    public LoginResponse studentLogin(StudentLoginRequest request) {
        // Load student by code
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                .loadStudentByCode(request.getStudentCode());

        // Check if student is active
        if (!userDetails.isEnabled()) {
            throw new ForbiddenException("Student account is disabled");
        }

        // Generate tokens (no email for student)
        String accessToken = jwtUtils.generateAccessToken(
                userDetails.getId(),
                request.getStudentCode(), // Use student code as identifier
                UserType.STUDENT
        );

        String refreshToken = jwtUtils.generateRefreshToken(
                userDetails.getId(),
                UserType.STUDENT
        );

        // Build response
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationInSeconds())
                .userType(UserType.STUDENT)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(userDetails.getId())
                        .fullName(userDetails.getFullName())
                        .email(null)
                        .avatarUrl(userDetails.getAvatarUrl())
                        .build())
                .build();
    }

    /**
     * Refresh access token using refresh token
     */
    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid or expired refresh token");
        }

        // Extract user info from refresh token
        String userId = jwtUtils.extractUserId(refreshToken);
        UserType userType = jwtUtils.extractUserType(refreshToken);

        // Generate new access token
        String newAccessToken = jwtUtils.generateAccessToken(userId, userId, userType);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId, userType);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getExpirationInSeconds())
                .userType(userType)
                .build();
    }
}
