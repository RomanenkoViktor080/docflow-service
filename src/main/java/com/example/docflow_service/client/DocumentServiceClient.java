package com.example.docflow_service.client;

import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentSubmitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "docflow-service",
        url = "${app.self-url}",
        path = "/api/v1/documents")
public interface DocumentServiceClient {
    @PostMapping
    DocumentDto create(@RequestBody DocumentCreateDto dto);

    @PostMapping("/submit")
    List<DocumentStatusChangeResponseDto> submitBatch(@RequestBody DocumentSubmitDto dto);

    @PostMapping("/approve")
    List<DocumentStatusChangeResponseDto> approveBatch(@RequestBody DocumentApproveDto dto);
}