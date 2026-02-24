package com.example.docflow_service.scheduler;

import com.example.docflow_service.client.DocumentServiceClient;
import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.repository.document.DocumentRepository;
import com.example.docflow_service.utils.aop.log.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
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

    @Loggable(startMessage = "Запуск фонового процесса APPROVE-worker")
    @Scheduled(fixedDelayString = "${workers.approve.delay:1000}")
    public void schedule() {
        List<Long> ids = repository.getIdsByStatus(DocumentStatus.SUBMITTED, batchSize);
        if (!ids.isEmpty()) {
            log.info("Найдено и отправлено {} документов", ids.size());
            client.approveBatch(new DocumentApproveDto(ids, SYSTEM_USER_ID, null));
        }
    }
}
