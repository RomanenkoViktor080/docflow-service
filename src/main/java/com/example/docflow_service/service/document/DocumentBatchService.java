package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentApproveRequestDto;
import com.example.docflow_service.dto.document.DocumentSubmitRequestDto;

import java.util.List;

public interface DocumentBatchService {
    List<DocumentStatusChangeResponseDto> submit(DocumentSubmitRequestDto dto);

    List<DocumentStatusChangeResponseDto> approve(DocumentApproveRequestDto dto);
}
