package com.example.docflow_service.exception.api;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    protected HttpStatus getDefaultStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
