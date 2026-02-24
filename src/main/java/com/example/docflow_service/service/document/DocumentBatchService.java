package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.dto.document.DocumentSubmitDto;

import java.util.List;

public interface DocumentBatchService {
    List<DocumentStatusChangeResponseDto> submit(DocumentSubmitDto dto);

    List<DocumentStatusChangeResponseDto> approve(DocumentApproveDto dto);
}
