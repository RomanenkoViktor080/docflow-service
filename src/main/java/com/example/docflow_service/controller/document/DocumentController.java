package com.example.docflow_service.controller.document;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.service.document.DocumentService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@Tag(name = "Document")
public class DocumentController {
    private final DocumentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentDto create(
            @RequestBody @Valid DocumentCreateDto dto
    ) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public DocumentViewDto find(
            @PathVariable("id") Long id
    ) {
        return service.find(id);
    }

    @GetMapping
    public Page<DocumentDto> get(
            @ParameterObject @Valid DocumentFilterDto dto,
            @ParameterObject Pageable pageable
    ) {
        return service.get(dto, pageable);
    }
}
