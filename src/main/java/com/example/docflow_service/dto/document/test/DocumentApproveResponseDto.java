package com.example.docflow_service.dto.document.test;

import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.entity.document.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record DocumentApproveResponseDto(
        @Schema(
                description = "Идентификатор документа, над которым выполнялось конкурентное утверждение",
                example = "101"
        )
        long documentId,

        @Schema(
                description = "Количество попыток утверждения, завершившихся успешно (ожидается ровно 1)",
                example = "1"
        )
        int successCount,

        @Schema(
                description = "Количество попыток, завершившихся конфликтом или ошибкой",
                example = "19"
        )
        int errorCount,

        @Schema(description = "Финальный статус документа после завершения всех параллельных попыток")
        DocumentStatus finalStatus,

        @Schema(
                description = "Количество записей в реестре утверждений для данного документа (ожидается 1)",
                example = "1"
        )
        long approvedCount,

        @Schema(description = "Список результатов по каждой отдельной попытке утверждения")
        List<DocumentStatusChangeResponseDto> responses
) {
}
