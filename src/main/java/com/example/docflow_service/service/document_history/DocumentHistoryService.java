package com.example.docflow_service.service.document_history;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;

public interface DocumentHistoryService {
    void create(
            long documentId,
            long initiatorId,
            DocumentAction action,
            String comment,
            DocumentStatus statusFrom,
            DocumentStatus statusTo
    );
}
