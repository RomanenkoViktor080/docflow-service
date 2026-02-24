package com.example.docflow_service.controller.document;

import com.example.docflow_service.dto.document.DocumentApproveDto;
import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.dto.document.DocumentStatusChangeResponseDto;
import com.example.docflow_service.dto.document.DocumentSubmitDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.service.document.DocumentBatchService;
import com.example.docflow_service.service.document.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@Tag(name = "Документы", description = "Управление жизненным циклом документов и пакетная обработка")
public class DocumentController {
    private final DocumentService documentService;
    private final DocumentBatchService documentBatchService;

    @Operation(summary = "Создать новый документ", description = "Создает черновик документа в системе")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentDto create(
            @RequestBody @Valid DocumentCreateDto dto
    ) {
        return documentService.create(dto);
    }

    @Operation(summary = "Получить документ с историей по ID")
    @GetMapping("/{id}")
    public DocumentViewDto find(
            @PathVariable("id") Long id
    ) {
        return documentService.find(id);
    }

    @Operation(summary = "Поиск документов с фильтрацией")
    @GetMapping
    public Page<DocumentDto> get(
            @ParameterObject @Valid DocumentFilterDto dto,
            @ParameterObject Pageable pageable
    ) {
        return documentService.get(dto, pageable);
    }

    @Operation(
            summary = "Отправить документы на согласование",
            description = "Массовая отправка документов. Возвращает статус обработки по каждому ID"
    )
    @PostMapping("/submit")
    public List<DocumentStatusChangeResponseDto> submit(
            @RequestBody @Valid DocumentSubmitDto dto
    ) {
        return documentBatchService.submit(dto);
    }

    @Operation(
            summary = "Утвердить документы",
            description = "Массовое утверждение документов"
    )
    @PostMapping("/approve")
    public List<DocumentStatusChangeResponseDto> approve(
            @RequestBody @Valid DocumentApproveDto dto
    ) {
        return documentBatchService.approve(dto);
    }
}
