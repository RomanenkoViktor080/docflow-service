package com.example.docflow_service.console;

import com.example.docflow_service.client.DocumentServiceClient;
import com.example.docflow_service.dto.document.DocumentCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.command.annotation.Command;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
@Command
@Component
public class DocumentGenerator {
    private static final long AUTHOR_ID = 1;
    private static final String GENERATED_TITLE = "generated";

    private final DocumentServiceClient client;
    @Qualifier("defaultTaskExecutor")
    private final Executor executor;
    @Value("${document.generator.quantity:25}")
    private int quantity;

    @Command(command = "generate", description = "Создает N документов через api")
    public void generate(
    ) {
        for (int i = 0; i < quantity; i++) {
            CompletableFuture.runAsync(() -> {
                client.create(new DocumentCreateDto(AUTHOR_ID, GENERATED_TITLE));
            }, executor);
        }
    }
}