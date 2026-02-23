package com.example.docflow_service.dto.document_history;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;
import lombok.Builder;

@Builder
public record DocumentHistoryCreateDto(
        Long documentId,
        Long initiatorId,
        DocumentAction action,
        DocumentStatus fromStatus,
        DocumentStatus toStatus,
        String comment
) {
}
