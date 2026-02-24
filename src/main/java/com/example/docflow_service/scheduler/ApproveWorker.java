package com.example.docflow_service.scheduler;

import com.example.docflow_service.client.DocumentServiceClient;
import com.example.docflow_service.dto.document.DocumentApproveRequestDto;
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
        prefix = "workers.approve",
        name = "enabled",
        havingValue = "true"
)
@Component
public class ApproveWorker {
    private static final long SYSTEM_USER_ID = 0;

    private final DocumentRepository repository;
    private final DocumentServiceClient client;
    @Value("${workers.approve.fetch-batch-size}")
    private int batchSize;

    @Scheduled(cron = "${workers.approve.cron}")
    public void schedule() {
        List<Long> ids = repository.getIdsByStatus(DocumentStatus.DRAFT, batchSize);
        if (!ids.isEmpty()) {
            client.approveBatch(new DocumentApproveRequestDto(ids, SYSTEM_USER_ID));
        }
    }
}
