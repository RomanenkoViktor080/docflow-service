package com.example.docflow_service.repository.document;

import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.exception.api.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    @Query(value = "SELECT nextval('document_number_seq')", nativeQuery = true)
    Long getNextDocumentNumber();

    @Query("""
            SELECT document FROM Document document LEFT JOIN FETCH document.history WHERE document.id = :id
            """)
    Optional<Document> findByIdWithHistory(long id);

    default Document findByIdWithHistoryOrThrow(long id) {
        return findByIdWithHistory(id).orElseThrow(
                () -> new EntityNotFoundException("Не найдено")
        );
    }


}
