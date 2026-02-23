package com.example.docflow_service.service.document_history;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;
import com.example.docflow_service.repository.document.DocumentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentHistoryServiceImpl implements DocumentHistoryService {
    private final DocumentHistoryRepository documentHistoryRepository;

    @Override
    @Transactional
    public void create(
            long documentId, long initiatorId,
            DocumentAction action, String comment,
            DocumentStatus statusFrom, DocumentStatus statusTo
    ) {
        documentHistoryRepository.create(
                documentId, initiatorId,
                action, comment,
                statusFrom, statusTo
        );
    }
}
