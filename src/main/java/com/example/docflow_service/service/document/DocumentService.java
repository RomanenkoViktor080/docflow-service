package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
    DocumentDto create(DocumentCreateDto dto);

    DocumentViewDto find(Long id);

    Page<DocumentDto> get(DocumentFilterDto dto, Pageable pageable);
}
