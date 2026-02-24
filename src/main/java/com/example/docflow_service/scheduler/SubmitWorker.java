package com.example.docflow_service.scheduler;

import com.example.docflow_service.client.DocumentServiceClient;
import com.example.docflow_service.dto.document.DocumentSubmitRequestDto;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.repository.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@ConditionalOnProperty(
        prefix = "workers.submit",
        name = "enabled",
        havingValue = "true"
)
@Component
public class SubmitWorker {
    private static final long SYSTEM_USER_ID = 0;

    private final DocumentRepository repository;
    private final DocumentServiceClient client;
    @Value("${workers.submit.fetch-batch-size}")
    private int batchSize;

    @Scheduled(cron = "${workers.submit.cron}")
    public void schedule() {
        List<Long> ids = repository.getIdsByStatus(DocumentStatus.SUBMITTED, batchSize);
        if (!ids.isEmpty()) {
            client.submitBatch(new DocumentSubmitRequestDto(ids, SYSTEM_USER_ID));
        }
    }
}
