package com.example.docflow_service.dto.document.test;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record DocumentApproveTestDto(
        @Schema(description = "Количество параллельных потоков", example = "10")
        Integer threads,

        @Schema(description = "Количество попыток утверждения на каждый поток", example = "10")
        @Min(value = 1, message = "Минимальное количество попыток 1")
        Integer attempts
) {
}
