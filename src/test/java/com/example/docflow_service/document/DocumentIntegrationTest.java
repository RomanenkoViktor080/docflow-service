package com.example.docflow_service.document;

import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentSubmitDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.repository.document.DocumentRepository;
import com.example.docflow_service.service.document.DocumentBatchService;
import com.example.docflow_service.service.document.DocumentService;
import com.example.docflow_service.service.document_approval.DocumentApprovalService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = "workers.submit.enabled=false")
@TestPropertySource(properties = "workers.approve.enabled=false")
class DocumentIntegrationTest {
    private static final long INITIATOR_ID = 1;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentBatchService documentBatchService;

    @Autowired
    private DocumentRepository documentRepository;

    @Mock
    private DocumentApprovalService docApprovalService;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:17.8");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Test
    void shouldCreateAndFindDocument() {
        DocumentCreateDto createDto = new DocumentCreateDto(1L, "Test Title");
        DocumentDto created = documentService.create(createDto);

        assertNotNull(created.id());
        assertEquals(DocumentStatus.DRAFT, created.status());

        DocumentViewDto found = documentService.find(created.id());
        assertEquals("Test Title", found.title());
    }

    @Test
    void shouldSubmitBatchCorrectly() {
        Long id1 = createTestDocument(DocumentStatus.DRAFT);
        Long id2 = createTestDocument(DocumentStatus.DRAFT);
        Long id3 = 9999L;
        List<Long> reqIds = List.of(id1, id2, id3);

        testCommit();

        DocumentSubmitDto request = new DocumentSubmitDto(reqIds, INITIATOR_ID, null);
        List<DocumentStatusChangeResponseDto> results = documentBatchService.submit(request);

        assertEquals(reqIds.size(), results.size());
        assertEquals(DocumentStatusChangeResponseDto.Status.SUCCESS, findStatus(results, id1));
        assertEquals(DocumentStatusChangeResponseDto.Status.NOT_FOUND, findStatus(results, id3));
        assertEquals(DocumentStatus.SUBMITTED, documentRepository.findByIdOrThrow(id1).getStatus());
    }

    @Test
    void shouldHandlePartialApproveResults() {
        Long idSuccess = createTestDocument(DocumentStatus.SUBMITTED);
        Long idConflict = createTestDocument(DocumentStatus.DRAFT);

        testCommit();

        DocumentApproveDto request = new DocumentApproveDto(List.of(idSuccess, idConflict), INITIATOR_ID, null);
        List<DocumentStatusChangeResponseDto> results = documentBatchService.approve(request);

        assertEquals(DocumentStatusChangeResponseDto.Status.SUCCESS, findStatus(results, idSuccess));
        assertEquals(DocumentStatusChangeResponseDto.Status.CONFLICT, findStatus(results, idConflict));
    }

    @Test
    void shouldRollbackStatusWhenRegistryFails() {
        Long docId = createTestDocument(DocumentStatus.SUBMITTED);

        doThrow(new RuntimeException("Registry Failure"))
                .when(docApprovalService).create(eq(docId), anyLong());

        DocumentApproveDto request = new DocumentApproveDto(List.of(docId), INITIATOR_ID, null);

        documentBatchService.approve(request);

        Document docAfter = documentRepository.findByIdOrThrow(docId);
        assertEquals(DocumentStatus.SUBMITTED, docAfter.getStatus());
    }

    private Long createTestDocument(DocumentStatus status) {
        Document doc = new Document();
        doc.setNumber(UUID.randomUUID().toString());
        doc.setTitle("Generated title");
        doc.setAuthorId(1L);
        doc.setStatus(status);
        return documentRepository.save(doc).getId();
    }

    private DocumentStatusChangeResponseDto.Status findStatus(
            List<DocumentStatusChangeResponseDto> list,
            Long id
    ) {
        return list.stream()
                .filter(response -> response.id().equals(id))
                .findFirst()
                .map(DocumentStatusChangeResponseDto::status)
                .orElse(null);
    }

    private void testCommit() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }
}