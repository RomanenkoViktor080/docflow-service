package com.example.docflow_service.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;

public record DocumentSubmitRequestDto(
        @Size(
                message = "Количество документов должно быть от 1 до 1000",
                min = 1, max = 1000
        )
        @Schema(description = "Список идентификаторов документов для пакетной отправки на согласование (от 1 до 1000)",
                example = "[1, 2, 3]"
        )
        HashSet<Long> documentIds,

        @NotNull(message = "Не указан инициатор")
        @Schema(description = "Идентификатор пользователя, выполняющего отправку на согласование")
        long initiatorId
) {
}
