package com.example.docflow_service.dto.document;

import com.example.docflow_service.entity.document.DocumentStatus;

import java.time.LocalDateTime;

public record DocumentDto(
        Long id,

        String number,

        Long authorId,

        String title,

        DocumentStatus status,

        LocalDateTime updatedAt
) {
}
