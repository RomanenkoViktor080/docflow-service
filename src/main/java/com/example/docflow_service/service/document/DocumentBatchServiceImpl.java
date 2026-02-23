package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentApproveRequestDto;
import com.example.docflow_service.dto.document.DocumentSubmitRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Service
public class DocumentBatchServiceImpl implements DocumentBatchService {
    private final DocumentService documentService;
    private final Executor executor;
    @Value("${document.batch-size:100}")
    private int chunkSize = 100;

    @Override
    public List<DocumentStatusChangeResponseDto> submit(DocumentSubmitRequestDto dto) {
        return processInBatches(dto.documentIds(), dto.initiatorId(), documentService::submit);
    }

    @Override
    public List<DocumentStatusChangeResponseDto> approve(DocumentApproveRequestDto dto) {
        return processInBatches(dto.documentIds(), dto.initiatorId(), documentService::approve);
    }

    private List<DocumentStatusChangeResponseDto> processInBatches(
            Set<Long> documentIds,
            long initiatorId,
            TriFunction<Long, Long, String, DocumentStatusChangeResponseDto> function
    ) {
        List<Long> ids = new ArrayList<>(documentIds);
        List<DocumentStatusChangeResponseDto> result = new LinkedList<>();
        List<CompletableFuture<List<DocumentStatusChangeResponseDto>>> futures = new ArrayList<>();
        for (int i = 0; i < ids.size(); i += chunkSize) {
            List<Long> chunk = ids.subList(i, Math.min(i + chunkSize, ids.size()));

            futures.add(
                    CompletableFuture.supplyAsync(
                            () -> chunk.stream().map(id -> function.apply(id, initiatorId, null)).toList(),
                            executor
                    )
            );
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        futures.forEach(future -> result.addAll(future.join()));

        return result;
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}
