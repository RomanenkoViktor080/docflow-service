package com.example.docflow_service.entity.filter.filter_item.document;

import com.example.docflow_service.dto.document.DocumentFilterDto;
import com.example.docflow_service.entity.document.Document;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class DocumentStatusFilter implements DocumentFilterInterface {
    @Override
    public boolean isApplicable(DocumentFilterDto dto) {
        return dto.status() != null;
    }

    @Override
    public Specification<Document> apply(Specification<Document> specification, DocumentFilterDto dto) {
        return specification.and((root, query, cb) ->
                cb.equal(root.get("status"), dto.status())
        );
    }
}
