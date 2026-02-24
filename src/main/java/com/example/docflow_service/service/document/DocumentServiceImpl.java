package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;
import com.example.docflow_service.entity.filter.builder.document.DocumentFilterBuilderInterface;
import com.example.docflow_service.mapper.DocumentMapper;
import com.example.docflow_service.repository.document.DocumentRepository;
import com.example.docflow_service.service.document_approval.DocumentApprovalService;
import com.example.docflow_service.service.document_history.DocumentHistoryService;
import com.example.docflow_service.utils.aop.log.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentHistoryService docHistoryService;
    private final DocumentApprovalService docApprovalService;
    private final DocumentMapper mapper;
    private final DocumentFilterBuilderInterface filterBuilder;
    @Value("${document.number-format:DOC-%06d}")
    private String format;

    @Override
    @Loggable(startMessage = "Создание документа")
    @Transactional
    public DocumentDto create(DocumentCreateDto dto) {
        log.info("Входные данные: {}", dto);
        Document document = mapper.toEntity(dto);
        document.setNumber(generateNumber());
        document.setStatus(DocumentStatus.DRAFT);

        return mapper.toDto(documentRepository.save(document));
    }

    @Override
    public DocumentViewDto find(Long id) {
        return mapper.toViewDto(documentRepository.findByIdWithHistoryOrThrow(id));
    }

    @Override
    public Page<DocumentDto> get(DocumentFilterDto dto, Pageable pageable) {
        Specification<Document> documentSpecification = filterBuilder.buildSpecification(dto);
        Page<Document> documents = documentRepository.findAll(documentSpecification, pageable);

        return documents.map(mapper::toDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public DocumentStatusChangeResponseDto submit(
            Long documentId,
            long initiatorId,
            String comment
    ) {
        boolean isUpdated = updateStatus(
                documentId, initiatorId,
                comment, DocumentAction.SUBMIT,
                DocumentStatus.DRAFT, DocumentStatus.SUBMITTED
        );
        if (isUpdated) {
            return new DocumentStatusChangeResponseDto(
                    documentId,
                    DocumentStatusChangeResponseDto.Status.SUCCESS
            );

        }
        DocumentStatusChangeResponseDto response = formErrorResponse(documentId);
        log.warn("Ошибка отправки на согласование, docId: {}, initId: {}, ответ: {}",
                documentId, initiatorId, response);
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public DocumentStatusChangeResponseDto approve(
            Long documentId,
            long initiatorId,
            String comment
    ) {
        boolean isUpdated = updateStatus(
                documentId, initiatorId,
                comment, DocumentAction.APPROVE,
                DocumentStatus.SUBMITTED, DocumentStatus.APPROVED
        );

        if (isUpdated) {
            docApprovalService.create(documentId, initiatorId);
            return new DocumentStatusChangeResponseDto(
                    documentId,
                    DocumentStatusChangeResponseDto.Status.SUCCESS
            );

        }
        DocumentStatusChangeResponseDto response = formErrorResponse(documentId);
        log.warn("Ошибка отправки на утверждение, docId: {}, initId: {}, ответ: {}",
                documentId, initiatorId, response);
        return response;
    }

    private DocumentStatusChangeResponseDto formErrorResponse(Long documentId) {
        Optional<Document> optionalDocument = documentRepository.findById(documentId);

        return optionalDocument
                .map(document ->
                        new DocumentStatusChangeResponseDto(
                                documentId,
                                DocumentStatusChangeResponseDto.Status.CONFLICT
                        ))
                .orElseGet(() ->
                        new DocumentStatusChangeResponseDto(
                                documentId,
                                DocumentStatusChangeResponseDto.Status.NOT_FOUND
                        ));
    }

    private boolean updateStatus(
            long documentId, long initiatorId,
            String comment, DocumentAction action,
            DocumentStatus statusFrom, DocumentStatus statusTo
    ) {
        Long updatedDocId = documentRepository.tryChangeStatus(
                documentId,
                statusFrom,
                statusTo
        );
        if (updatedDocId != null && updatedDocId.equals(documentId)) {
            docHistoryService.create(
                    documentId,
                    initiatorId,
                    action,
                    comment,
                    statusFrom,
                    statusTo
            );
            return true;
        }
        return false;
    }

    private String generateNumber() {
        return String.format(format, documentRepository.getNextDocumentNumber());
    }
}
