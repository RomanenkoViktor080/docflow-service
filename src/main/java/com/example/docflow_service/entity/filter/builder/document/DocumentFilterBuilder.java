package com.example.docflow_service.entity.filter.builder.document;

import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.filter.builder.BaseFilterBuilder;
import com.example.docflow_service.entity.filter.filter_item.document.DocumentFilterInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentFilterBuilder extends BaseFilterBuilder<Document, DocumentFilterDto>
        implements DocumentFilterBuilderInterface {
    private final List<DocumentFilterInterface> filters;

    @Override
    public List<DocumentFilterInterface> getFilters() {
        return filters;
    }
}
