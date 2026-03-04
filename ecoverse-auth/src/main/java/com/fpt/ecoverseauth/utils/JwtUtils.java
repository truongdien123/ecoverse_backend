package com.fpt.ecoverseauth.utils;

import com.fpt.ecoverseauth.enums.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT token operations
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret:ecoverse-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration; // 24 hours in milliseconds

    @Value("${jwt.refresh-expiration:604800000}")
    private Long jwtRefreshExpiration; // 7 days in milliseconds

    /**
     * Generate secret key from string
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate access token
     */
    public String generateAccessToken(String userId, String email, UserType userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("userType", userType.name());
        claims.put("tokenType", "ACCESS");

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String userId, UserType userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType.name());
        claims.put("tokenType", "REFRESH");

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extract user ID from token
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract user type from token
     */
    public UserType extractUserType(String token) {
        String userTypeStr = extractClaim(token, claims -> claims.get("userType", String.class));
        return UserType.valueOf(userTypeStr);
    }

    /**
     * Extract email from token
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Check if token is expired
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Validate token
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Get JWT expiration time in seconds
     */
    public Long getExpirationInSeconds() {
        return jwtExpiration / 1000;
    }
}
