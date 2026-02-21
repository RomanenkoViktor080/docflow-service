package com.example.docflow_service.mapper;

import com.example.docflow_service.dto.document.DocumentDto;
import com.example.docflow_service.entity.document.Document;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {
    DocumentDto toDto(Document entity);
}
