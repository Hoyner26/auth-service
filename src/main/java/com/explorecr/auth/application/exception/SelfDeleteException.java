package com.explorecr.auth.application.exception;

import org.springframework.http.HttpStatus;

public class SelfDeleteException extends ApiException {
    public SelfDeleteException() {
        super("Cannot delete your own account", HttpStatus.FORBIDDEN);
    }
}
