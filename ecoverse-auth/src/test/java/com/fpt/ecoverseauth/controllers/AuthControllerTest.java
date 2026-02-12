package com.fpt.ecoverseauth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoverseauth.dto.request.LoginRequest;
import com.fpt.ecoverseauth.dto.request.StudentLoginRequest;
import com.fpt.ecoverseauth.dto.response.LoginResponse;
import com.fpt.ecoverseauth.enums.UserType;
import com.fpt.ecoverseauth.services.AuthService;
import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/auth/login - Success")
    void login_Success() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("admin@ecoverse.com", "password123", UserType.ADMIN);
        
        LoginResponse response = LoginResponse.builder()
                .accessToken("access-token-123")
                .refreshToken("refresh-token-456")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userType(UserType.ADMIN)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id("admin-uuid")
                        .fullName("Admin User")
                        .email("admin@ecoverse.com")
                        .avatarUrl(null)
                        .build())
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.accessToken").value("access-token-123"))
                .andExpect(jsonPath("$.data.userType").value("ADMIN"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Invalid Email Format")
    void login_InvalidEmailFormat() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("invalid-email", "password123", UserType.ADMIN);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Missing Password")
    void login_MissingPassword() throws Exception {
        // Arrange
        String requestJson = "{\"email\":\"admin@ecoverse.com\",\"userType\":\"ADMIN\"}";

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - User Not Found")
    void login_UserNotFound() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest("notfound@ecoverse.com", "password123", UserType.ADMIN);

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new NotFoundException("Admin not found with email: notfound@ecoverse.com"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/auth/student/login - Success")
    void studentLogin_Success() throws Exception {
        // Arrange
        StudentLoginRequest request = new StudentLoginRequest("STU001");

        LoginResponse response = LoginResponse.builder()
                .accessToken("student-access-token")
                .refreshToken("student-refresh-token")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userType(UserType.STUDENT)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id("student-uuid")
                        .fullName("Student User")
                        .email(null)
                        .avatarUrl(null)
                        .build())
                .build();

        when(authService.studentLogin(any(StudentLoginRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student login successful"))
                .andExpect(jsonPath("$.data.userType").value("STUDENT"));
    }

    @Test
    @DisplayName("POST /api/auth/student/login - Student Not Found")
    void studentLogin_StudentNotFound() throws Exception {
        // Arrange
        StudentLoginRequest request = new StudentLoginRequest("INVALID_CODE");

        when(authService.studentLogin(any(StudentLoginRequest.class)))
                .thenThrow(new NotFoundException("Student not found with code: INVALID_CODE"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/student/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Success")
    void refreshToken_Success() throws Exception {
        // Arrange
        LoginResponse response = LoginResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .userType(UserType.ADMIN)
                .build();

        when(authService.refreshToken("valid-refresh-token")).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .param("refreshToken", "valid-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"));
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Invalid Token")
    void refreshToken_InvalidToken() throws Exception {
        // Arrange
        when(authService.refreshToken("invalid-token"))
                .thenThrow(new BadRequestException("Invalid or expired refresh token"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .param("refreshToken", "invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/auth/logout - Success")
    void logout_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }
}
