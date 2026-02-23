package com.example.docflow_service.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record DocumentStatusChangeResponseDto(
        @Schema(
                description = "Идентификатор документа",
                example = "10"
        )
        Long id,

        @Schema(description = "Результат выполнения операции изменения статуса")
        Status status
) {
    public enum Status {
        @Schema(description = "Операция выполнена успешно")
        SUCCESS,

        @Schema(description = "Документ не найден")
        NOT_FOUND,

        @Schema(description = "Недопустимо изменение статуса (конфликт)")
        CONFLICT
    }
}
