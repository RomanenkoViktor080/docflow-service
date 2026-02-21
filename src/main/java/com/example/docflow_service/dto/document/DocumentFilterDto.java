package com.example.docflow_service.dto.document;

import com.example.docflow_service.entity.document.DocumentStatus;

import java.time.LocalDateTime;

public record DocumentFilterDto(
        DocumentStatus status,

        Long authorId,

        LocalDateTime from,

        LocalDateTime to
) {
}
