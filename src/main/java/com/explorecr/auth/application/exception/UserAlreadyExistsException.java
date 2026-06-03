package com.explorecr.auth.application.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException() {
        super("Email already in use", HttpStatus.CONFLICT);
    }
}
