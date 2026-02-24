package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentSubmitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentBatchServiceImpl implements DocumentBatchService {
    private final DocumentService documentService;
    @Qualifier("defaultTaskExecutor")
    private final Executor executor;

    @Value("${document.batch-size:100}")
    private int chunkSize;
    @Value("${document.log-step-percent:5}")
    private int logStepPercent;

    @Override
    public List<DocumentStatusChangeResponseDto> submit(DocumentSubmitDto dto) {
        log.info("Полученно {} документов на batch согласование", dto.documentIds().size());
        return processInBatches(dto.documentIds(), dto.initiatorId(), dto.comment(), documentService::submit);
    }

    @Override
    public List<DocumentStatusChangeResponseDto> approve(DocumentApproveDto dto) {
        log.info("Полученно {} документов на batch утверждение", dto.documentIds().size());
        return processInBatches(dto.documentIds(), dto.initiatorId(), dto.comment(), documentService::approve);
    }

    private List<DocumentStatusChangeResponseDto> processInBatches(
            List<Long> ids,
            long initiatorId,
            String comment,
            TriFunction<Long, Long, String, DocumentStatusChangeResponseDto> function
    ) {
        int total = ids.size();
        double step = Math.max(1.0, total * (logStepPercent / 100.0));
        AtomicInteger processedCount = new AtomicInteger();
        List<DocumentStatusChangeResponseDto> result = new LinkedList<>();
        List<CompletableFuture<List<DocumentStatusChangeResponseDto>>> futures = new ArrayList<>();
        for (int i = 0; i < ids.size(); i += chunkSize) {
            List<Long> chunk = ids.subList(i, Math.min(i + chunkSize, ids.size()));

            futures.add(
                    CompletableFuture.supplyAsync(
                            () -> chunk.stream().map(id -> function.apply(id, initiatorId, comment)).toList(),
                            executor
                    ).thenApply(res -> {
                        logStep(processedCount, chunk, total, step);
                        return res;
                    })
            );
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        futures.forEach(future -> result.addAll(future.join()));
        log.info("Обработано {} документов", ids.size());
        return result;
    }

    private static void logStep(AtomicInteger processedCount, List<Long> chunk, int total, double step) {
        int current = processedCount.addAndGet(chunk.size());
        if (current == total || (current / step > (current - chunk.size()) / step)) {
            double percent = (double) current / total * 100;
            log.info("Прогресс обработки: {}/{} ({}%)", current, total, String.format("%.1f", percent));
        }
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}
