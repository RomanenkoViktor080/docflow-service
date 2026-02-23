package com.example.docflow_service.dto.document;

import java.util.HashSet;

public record DocumentSubmitRequestDto(
        HashSet<Long> documentIds,
        long initiatorId
) {
}
