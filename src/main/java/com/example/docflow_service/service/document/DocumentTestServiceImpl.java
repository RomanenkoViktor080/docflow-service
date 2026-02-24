package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.test.DocumentApproveResponseDto;
import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.repository.document.DocumentApprovalRepository;
import com.example.docflow_service.repository.document.DocumentRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class DocumentTestServiceImpl implements DocumentTestService {
    private static final long INITIATOR_ID = 1;
    private static final long AUTHOR_ID = 1;
    private static final String TEST_TEXT = "test";

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;
    private final DocumentApprovalRepository documentApprovalRepository;
    @Qualifier("defaultTaskExecutor")
    private final Executor executor;
    private final EntityManager entityManager;

    @Value("${document.test.threads:20}")
    private int threads;
    @Value("${document.test.responses:20}")
    private int attempts;

    @Override
    public DocumentApproveResponseDto approve() {
        List<DocumentStatusChangeResponseDto> responses = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger errorCount = new AtomicInteger();
        AtomicInteger successCount = new AtomicInteger();
        CountDownLatch ready = new CountDownLatch(threads);

        DocumentDto documentDto = documentService.create(new DocumentCreateDto(AUTHOR_ID, TEST_TEXT));
        documentService.submit(documentDto.id(), INITIATOR_ID, TEST_TEXT);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(CompletableFuture.runAsync(() -> {
                ready.countDown();
                for (int j = 0; j < attempts; j++) {
                    DocumentStatusChangeResponseDto response = documentService.approve(
                            documentDto.id(),
                            INITIATOR_ID,
                            TEST_TEXT
                    );
                    if (response.status().equals(DocumentStatusChangeResponseDto.Status.SUCCESS)) {
                        successCount.incrementAndGet();
                    } else {
                        errorCount.incrementAndGet();
                    }
                    responses.add(response);

                }
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        entityManager.clear();

        Document document = documentRepository.findByIdOrThrow(documentDto.id());
        long approvedCount = documentApprovalRepository.countByDocumentId(documentDto.id());

        return DocumentApproveResponseDto.builder()
                .documentId(documentDto.id())
                .successCount(successCount.get())
                .errorCount(errorCount.get())
                .finalStatus(document.getStatus())
                .approvedCount(approvedCount)
                .responses(responses)
                .build();
    }
}
