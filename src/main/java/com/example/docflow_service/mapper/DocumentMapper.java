package com.example.docflow_service.mapper;

import com.example.docflow_service.dto.document.DocumentCreateDto;
import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.dto.document.DocumentViewDto;
import com.example.docflow_service.entity.document.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = DocumentHistoryMapper.class
)
public interface DocumentMapper {
    DocumentDto toDto(Document entity);

    @Mapping(source = "history", target = "history", qualifiedByName = "toHistoryListDto")
    DocumentViewDto toViewDto(Document entity);

    Document toEntity(DocumentCreateDto dto);
}
