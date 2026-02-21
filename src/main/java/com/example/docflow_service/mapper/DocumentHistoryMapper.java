package com.example.docflow_service.mapper;

import com.example.docflow_service.dto.document_history.DocumentHistoryDto;
import com.example.docflow_service.entity.document_history.DocumentHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DocumentHistoryMapper {
    DocumentHistoryDto toDto(DocumentHistory entity);

    @Named("toHistoryListDto")
    List<DocumentHistoryDto> toListDto(List<DocumentHistory> entities);
}
