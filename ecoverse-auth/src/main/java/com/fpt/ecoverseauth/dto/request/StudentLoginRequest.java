package com.fpt.ecoverseauth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Login request DTO for Student users (using student code instead of email)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginRequest {

    @NotBlank(message = "Student code is required")
    private String studentCode;

}
