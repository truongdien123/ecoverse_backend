package com.fpt.ecoversenotification.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParentCredentialMail {
    private final String email;
    private final String fullName;
    private final String rawPassword;
}
