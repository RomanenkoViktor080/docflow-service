package com.example.docflow_service.dto.document.test;

import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.entity.document.DocumentStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record DocumentApproveResponseDto(
        long documentId,
        int successCount,
        int errorCount,
        DocumentStatus finalStatus,
        long approvedCount,
        List<DocumentStatusChangeResponseDto> responses
) {
}
