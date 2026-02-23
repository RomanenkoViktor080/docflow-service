package com.example.docflow_service.service.document_approval;

import com.example.docflow_service.repository.document.DocumentApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentApprovalServiceImpl implements DocumentApprovalService {
    private final DocumentApprovalRepository approvalRepository;

    @Override
    @Transactional
    public void create(long documentId, long initiatorId) {
        approvalRepository.create(documentId, initiatorId);
    }
}
