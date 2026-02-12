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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private CustomUserDetails adminUserDetails;
    private CustomUserDetails parentUserDetails;
    private CustomUserDetails studentUserDetails;

    @BeforeEach
    void setUp() {
        // Setup Admin user details
        adminUserDetails = CustomUserDetails.builder()
                .id("admin-uuid-123")
                .email("admin@ecoverse.com")
                .password("$2a$10$encodedPassword")
                .fullName("Admin User")
                .avatarUrl(null)
                .userType(UserType.ADMIN)
                .active(true)
                .build();

        // Setup Parent user details
        parentUserDetails = CustomUserDetails.builder()
                .id("parent-uuid-456")
                .email("parent@example.com")
                .password("$2a$10$encodedPassword")
                .fullName("Parent User")
                .avatarUrl(null)
                .userType(UserType.PARENT)
                .active(true)
                .build();

        // Setup Student user details
        studentUserDetails = CustomUserDetails.builder()
                .id("student-uuid-789")
                .email(null)
                .password(null)
                .fullName("Student User")
                .avatarUrl(null)
                .userType(UserType.STUDENT)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Login Admin - Success")
    void login_Admin_Success() {
        // Arrange
        LoginRequest request = new LoginRequest("admin@ecoverse.com", "password123", UserType.ADMIN);

        when(userDetailsService.loadUserByEmailAndType("admin@ecoverse.com", UserType.ADMIN))
                .thenReturn(adminUserDetails);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword"))
                .thenReturn(true);
        when(jwtUtils.generateAccessToken(anyString(), anyString(), any(UserType.class)))
                .thenReturn("access-token-123");
        when(jwtUtils.generateRefreshToken(anyString(), any(UserType.class)))
                .thenReturn("refresh-token-456");
        when(jwtUtils.getExpirationInSeconds())
                .thenReturn(86400L);

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access-token-123", response.getAccessToken());
        assertEquals("refresh-token-456", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(UserType.ADMIN, response.getUserType());
        assertEquals("admin-uuid-123", response.getUserInfo().getId());
        assertEquals("Admin User", response.getUserInfo().getFullName());

        verify(userDetailsService).loadUserByEmailAndType("admin@ecoverse.com", UserType.ADMIN);
        verify(passwordEncoder).matches("password123", "$2a$10$encodedPassword");
    }

    @Test
    @DisplayName("Login Admin - Invalid Password")
    void login_Admin_InvalidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest("admin@ecoverse.com", "wrongpassword", UserType.ADMIN);

        when(userDetailsService.loadUserByEmailAndType("admin@ecoverse.com", UserType.ADMIN))
                .thenReturn(adminUserDetails);
        when(passwordEncoder.matches("wrongpassword", "$2a$10$encodedPassword"))
                .thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.login(request);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Login - Account Disabled")
    void login_AccountDisabled() {
        // Arrange
        adminUserDetails.setActive(false);
        LoginRequest request = new LoginRequest("admin@ecoverse.com", "password123", UserType.ADMIN);

        when(userDetailsService.loadUserByEmailAndType("admin@ecoverse.com", UserType.ADMIN))
                .thenReturn(adminUserDetails);
        when(passwordEncoder.matches("password123", "$2a$10$encodedPassword"))
                .thenReturn(true);

        // Act & Assert
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            authService.login(request);
        });

        assertEquals("Account is disabled", exception.getMessage());
    }

    @Test
    @DisplayName("Login Student - Should Throw BadRequestException")
    void login_Student_ShouldThrowBadRequestException() {
        // Arrange
        LoginRequest request = new LoginRequest("student@example.com", "password", UserType.STUDENT);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.login(request);
        });

        assertEquals("Students should use student code to login", exception.getMessage());
    }

    @Test
    @DisplayName("Student Login - Success")
    void studentLogin_Success() {
        // Arrange
        StudentLoginRequest request = new StudentLoginRequest("STU001");

        when(userDetailsService.loadStudentByCode("STU001"))
                .thenReturn(studentUserDetails);
        when(jwtUtils.generateAccessToken(anyString(), anyString(), any(UserType.class)))
                .thenReturn("student-access-token");
        when(jwtUtils.generateRefreshToken(anyString(), any(UserType.class)))
                .thenReturn("student-refresh-token");
        when(jwtUtils.getExpirationInSeconds())
                .thenReturn(86400L);

        // Act
        LoginResponse response = authService.studentLogin(request);

        // Assert
        assertNotNull(response);
        assertEquals("student-access-token", response.getAccessToken());
        assertEquals("student-refresh-token", response.getRefreshToken());
        assertEquals(UserType.STUDENT, response.getUserType());
        assertNull(response.getUserInfo().getEmail());

        verify(userDetailsService).loadStudentByCode("STU001");
    }

    @Test
    @DisplayName("Refresh Token - Success")
    void refreshToken_Success() {
        // Arrange
        String refreshToken = "valid-refresh-token";

        when(jwtUtils.validateToken(refreshToken)).thenReturn(true);
        when(jwtUtils.extractUserId(refreshToken)).thenReturn("user-id-123");
        when(jwtUtils.extractUserType(refreshToken)).thenReturn(UserType.ADMIN);
        when(jwtUtils.generateAccessToken(anyString(), anyString(), any(UserType.class)))
                .thenReturn("new-access-token");
        when(jwtUtils.generateRefreshToken(anyString(), any(UserType.class)))
                .thenReturn("new-refresh-token");
        when(jwtUtils.getExpirationInSeconds()).thenReturn(86400L);

        // Act
        LoginResponse response = authService.refreshToken(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
    }

    @Test
    @DisplayName("Refresh Token - Invalid Token")
    void refreshToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";

        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.refreshToken(invalidToken);
        });

        assertEquals("Invalid or expired refresh token", exception.getMessage());
    }
}
