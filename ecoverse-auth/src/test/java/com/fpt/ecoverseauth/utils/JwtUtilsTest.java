package com.fpt.ecoverseauth.utils;

import com.fpt.ecoverseauth.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set values using reflection since @Value won't work in unit tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", 
                "ecoverse-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtUtils, "jwtRefreshExpiration", 604800000L);
    }

    @Test
    @DisplayName("Generate Access Token - Success")
    void generateAccessToken_Success() {
        // Arrange
        String userId = "user-123";
        String email = "test@ecoverse.com";
        UserType userType = UserType.ADMIN;

        // Act
        String token = jwtUtils.generateAccessToken(userId, email, userType);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT format: header.payload.signature
    }

    @Test
    @DisplayName("Generate Refresh Token - Success")
    void generateRefreshToken_Success() {
        // Arrange
        String userId = "user-123";
        UserType userType = UserType.PARENT;

        // Act
        String token = jwtUtils.generateRefreshToken(userId, userType);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extract UserId from Token - Success")
    void extractUserId_Success() {
        // Arrange
        String userId = "user-uuid-123";
        String token = jwtUtils.generateAccessToken(userId, "test@email.com", UserType.ADMIN);

        // Act
        String extractedUserId = jwtUtils.extractUserId(token);

        // Assert
        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Extract UserType from Token - Success")
    void extractUserType_Success() {
        // Arrange
        UserType expectedUserType = UserType.PARTNERSHIP;
        String token = jwtUtils.generateAccessToken("user-123", "test@email.com", expectedUserType);

        // Act
        UserType extractedUserType = jwtUtils.extractUserType(token);

        // Assert
        assertEquals(expectedUserType, extractedUserType);
    }

    @Test
    @DisplayName("Extract Email from Token - Success")
    void extractEmail_Success() {
        // Arrange
        String expectedEmail = "admin@ecoverse.com";
        String token = jwtUtils.generateAccessToken("user-123", expectedEmail, UserType.ADMIN);

        // Act
        String extractedEmail = jwtUtils.extractEmail(token);

        // Assert
        assertEquals(expectedEmail, extractedEmail);
    }

    @Test
    @DisplayName("Validate Token - Valid Token")
    void validateToken_ValidToken() {
        // Arrange
        String token = jwtUtils.generateAccessToken("user-123", "test@email.com", UserType.ADMIN);

        // Act
        Boolean isValid = jwtUtils.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validate Token - Invalid Token")
    void validateToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Boolean isValid = jwtUtils.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Validate Token - Malformed Token")
    void validateToken_MalformedToken() {
        // Arrange
        String malformedToken = "not-a-jwt-token";

        // Act
        Boolean isValid = jwtUtils.validateToken(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Token Not Expired - Fresh Token")
    void isTokenExpired_FreshToken() {
        // Arrange
        String token = jwtUtils.generateAccessToken("user-123", "test@email.com", UserType.ADMIN);

        // Act
        Boolean isExpired = jwtUtils.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Get Expiration In Seconds")
    void getExpirationInSeconds() {
        // Act
        Long expirationSeconds = jwtUtils.getExpirationInSeconds();

        // Assert
        assertEquals(86400L, expirationSeconds); // 24 hours
    }

    @Test
    @DisplayName("Different Users Generate Different Tokens")
    void differentUsers_GenerateDifferentTokens() {
        // Arrange & Act
        String adminToken = jwtUtils.generateAccessToken("admin-123", "admin@email.com", UserType.ADMIN);
        String parentToken = jwtUtils.generateAccessToken("parent-456", "parent@email.com", UserType.PARENT);

        // Assert
        assertNotEquals(adminToken, parentToken);
    }

    @Test
    @DisplayName("Access and Refresh Tokens are Different")
    void accessAndRefreshTokens_AreDifferent() {
        // Arrange
        String userId = "user-123";
        UserType userType = UserType.STUDENT;

        // Act
        String accessToken = jwtUtils.generateAccessToken(userId, "test@email.com", userType);
        String refreshToken = jwtUtils.generateRefreshToken(userId, userType);

        // Assert
        assertNotEquals(accessToken, refreshToken);
    }
}
