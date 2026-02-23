package com.example.docflow_service.dto.error;

public record ValidationErrorDetail(
        String field,
        String message,
        Object rejectedValue
) {
}
