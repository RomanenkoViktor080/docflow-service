package com.example.docflow_service.client;

import com.example.docflow_service.dto.document.DocumentApproveRequestDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentSubmitRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "docflow-service",
        url = "${app.self-url}",
        path = "/api/v1/documents")
public interface DocumentServiceClient {

    @PostMapping("/submit")
    List<DocumentStatusChangeResponseDto> submitBatch(@RequestBody DocumentSubmitRequestDto dto);

    @PostMapping("/approve")
    List<DocumentStatusChangeResponseDto> approveBatch(@RequestBody DocumentApproveRequestDto dto);
}