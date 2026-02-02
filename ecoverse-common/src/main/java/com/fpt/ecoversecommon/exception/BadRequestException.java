package com.fpt.ecoversecommon.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException{
    private HttpStatus status;

    public BadRequestException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
