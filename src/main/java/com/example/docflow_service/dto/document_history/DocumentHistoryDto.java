package com.example.docflow_service.dto.document_history;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record DocumentHistoryDto(
        @Schema(
                description = "Идентификатор записи истории",
                example = "15"
        )
        Long id,

        @Schema(
                description = "Идентификатор пользователя, инициировавшего изменение",
                example = "42"
        )
        Long initiatorId,

        @Schema(
                description = "Тип выполненного действия"
        )
        DocumentAction action,

        @Schema(
                description = "Статус документа до изменения"
        )
        DocumentStatus fromStatus,

        @Schema(
                description = "Статус документа после изменения"
        )
        DocumentStatus toStatus,

        @Schema(
                description = "Комментарий к изменению",
                example = "Документ готов к утверждению"
        )
        @Size(max = 255, message = "Комментарий не должен превышать 255 символов")
        String comment,

        @Schema(
                description = "Дата и время изменения",
                example = "2025-01-10T14:30:00"
        )
        LocalDateTime createdAt
) {
}
