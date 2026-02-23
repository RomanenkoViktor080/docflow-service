package com.example.docflow_service.exception.api;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
    public ConflictException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public ConflictException(String message) {
        super(message);
    }

    @Override
    protected HttpStatus getDefaultStatus() {
        return HttpStatus.CONFLICT;
    }
}
