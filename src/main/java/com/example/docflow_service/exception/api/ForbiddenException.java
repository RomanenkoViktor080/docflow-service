package com.example.docflow_service.exception.api;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    protected HttpStatus getDefaultStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
