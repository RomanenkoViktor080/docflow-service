package com.example.docflow_service.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DocumentApproveDto(
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
        @Schema(description = "Идентификатор пользователя, выполняющего утверждение", example = "1")
        Long initiatorId,

        @Size(max = 255, message = "Комментарий не может превышать 255 символов")
        @Schema(
                description = "Обоснование утверждения или произвольный комментарий",
                example = "Согласовано согласно регламенту №15"
        )
        String comment
) {
}
