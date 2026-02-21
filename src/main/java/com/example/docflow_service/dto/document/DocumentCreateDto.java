package com.example.docflow_service.dto.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentCreateDto(
        @NotNull(message = "Укажите автора")
        @Schema(description = "Идентификатор автора документа", example = "1")
        Long authorId,

        @Size(min = 1, max = 255, message = "Название документа должно быть от 1 до 255 символов")
        @NotBlank(message = "Введите название")
        @Schema(description = "Название документа")
        String title
) {
}
