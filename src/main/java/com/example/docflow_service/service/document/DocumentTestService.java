package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.test.DocumentApproveResponseDto;
import com.example.docflow_service.dto.document.test.DocumentApproveTestDto;

public interface DocumentTestService {
    DocumentApproveResponseDto approve(DocumentApproveTestDto dto);
}
