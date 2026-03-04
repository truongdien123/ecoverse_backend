package com.fpt.ecoverseauth.dto.response;

import com.fpt.ecoverseauth.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Login response DTO containing JWT token and user information
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private UserType userType;
    private UserInfo userInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private String id;
        private String fullName;
        private String email;
        private String avatarUrl;
    }
}
