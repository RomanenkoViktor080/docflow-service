package com.example.docflow_service.dto.document_history;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;

import java.time.LocalDateTime;

public record DocumentHistoryDto(
        Long id,
        Long initiatorId,
        DocumentAction action,
        DocumentStatus fromStatus,
        DocumentStatus toStatus,
        String comment,
        LocalDateTime createdAt
) {
}
