package com.example.docflow_service.dto.document;

import com.example.docflow_service.entity.document.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record DocumentFilterDto(
        @Schema(description = "Статус документа")
        DocumentStatus status,

        @Schema(description = "Идентификатор автора документа")
        Long authorId,

        @Schema(
                description = "Начало периода. Фильтрация по дате создания документа",
                example = "2026-03-01T10:00:00",
                format = "date-time"
        )
        LocalDateTime fromDate,

        @Schema(
                description = "Конец периода. Фильтрация выполняется по дате создания документа",
                example = "2026-03-01T10:00:00",
                format = "date-time"
        )
        LocalDateTime toDate
) {
}
