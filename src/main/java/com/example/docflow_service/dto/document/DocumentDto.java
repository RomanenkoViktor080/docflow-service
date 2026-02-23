package com.example.docflow_service.dto.document;

import com.example.docflow_service.entity.document.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record DocumentDto(
        @Schema(
                description = "Внутренний идентификатор документа",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Уникальный номер документа, генерируется автоматически",
                example = "DOC-000001"
        )
        String number,

        @Schema(
                description = "Идентификатор автора документа",
                example = "1"
        )
        Long authorId,

        @Schema(
                description = "Название документа",
                example = "Договор поставки",
                maxLength = 255
        )
        String title,

        @Schema(description = "Текущий статус документа")
        DocumentStatus status,

        @Schema(
                description = "Дата и время последнего обновления документа",
                example = "2026-03-01T10:00:00",
                format = "date-time"
        )
        LocalDateTime updatedAt
) {
}
