package com.example.docflow_service.repository.document;

import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.exception.api.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
    @Query(value = "SELECT nextval('document_number_seq')", nativeQuery = true)
    Long getNextDocumentNumber();

    @Query("SELECT document FROM Document document LEFT JOIN FETCH document.history WHERE document.id = :id")
    Optional<Document> findByIdWithHistory(long id);

    default Document findByIdWithHistoryOrThrow(long id) {
        return findByIdWithHistory(id).orElseThrow(
                () -> new EntityNotFoundException("Документ не найден", "Документ не найден, id: " + id)
        );
    }

    @Query(value = """
                UPDATE documents
                SET status = :#{#newStatus?.name()}
                WHERE id = :id
                  AND status = :#{#expectedStatus?.name()}
                RETURNING id
            """, nativeQuery = true)
    Long tryChangeStatus(
            Long id,
            DocumentStatus expectedStatus,
            DocumentStatus newStatus
    );

    default Document findByIdOrThrow(long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("Документ не найден", "Документ не найден, id: " + id)
        );
    }

    @Query(value = "SELECT id FROM documents WHERE status = :#{#status?.name()} ORDER BY created_at LIMIT :limit",
            nativeQuery = true)
    List<Long> getIdsByStatus(DocumentStatus status, int limit);
}
