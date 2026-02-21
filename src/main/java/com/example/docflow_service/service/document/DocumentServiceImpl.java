package com.example.docflow_service.service.document;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.filter.builder.document.AdminCardFilterBuilderInterface;
import com.example.docflow_service.mapper.DocumentMapper;
import com.example.docflow_service.repository.document.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;
    private final DocumentMapper mapper;
    private final AdminCardFilterBuilderInterface filterBuilder;
    @Value("${document.format:DOC-%06d}")
    private String format;

    @Override
    @Transactional
    public DocumentDto create(DocumentCreateDto dto) {
        Document document = mapper.toEntity(dto);
        document.setNumber(generateNumber());
        document.setStatus(DocumentStatus.DRAFT);

        return mapper.toDto(repository.save(document));
    }

    @Override
    public DocumentViewDto find(Long id) {
        return mapper.toViewDto(repository.findByIdWithHistoryOrThrow(id));
    }

    @Override
    public Page<DocumentDto> get(DocumentFilterDto dto, Pageable pageable) {
        Specification<Document> documentSpecification = filterBuilder.buildSpecification(dto);

        Page<Document> documents = repository.findAll(documentSpecification, pageable);

        return documents.map(mapper::toDto);
    }

    private String generateNumber() {
        return String.format(format, repository.getNextDocumentNumber());
    }
}
