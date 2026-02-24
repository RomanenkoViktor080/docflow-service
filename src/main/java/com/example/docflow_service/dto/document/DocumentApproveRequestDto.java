package com.example.docflow_service.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DocumentApproveRequestDto(
        @Size(
                message = "Количество документов должно быть от 1 до 1000",
                min = 1, max = 1000
        )
        @Schema(
                description = "Список идентификаторов документов для пакетного утверждения (от 1 до 1000)",
                example = "[1, 2, 3]"
        )
        List<Long> documentIds,

        @NotNull(message = "Не указан инициатор")
        @Schema(description = "Идентификатор пользователя, выполняющего утверждение")
        Long initiatorId
) {
}
