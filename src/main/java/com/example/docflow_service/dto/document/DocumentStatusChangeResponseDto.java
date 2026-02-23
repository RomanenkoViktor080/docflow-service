package com.example.docflow_service.dto.document;

import lombok.Builder;

@Builder
public record DocumentStatusChangeResponseDto(
        Long id,
        Status status
) {
    public enum Status {
        SUCCESS,
        NOT_FOUND,
        CONFLICT
    }
}
