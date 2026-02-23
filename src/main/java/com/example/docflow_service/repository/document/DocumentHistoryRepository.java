package com.example.docflow_service.repository.document;

import com.example.docflow_service.entity.document.DocumentStatus;
import com.example.docflow_service.entity.document_history.DocumentAction;
import com.example.docflow_service.entity.document_history.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO document_history (document_id,initiator_id,action,from_status,to_status,comment)
            VALUES (:documentId, :initiatorId, :#{#action?.name()}, 
                                :#{#statusFrom?.name()}, :#{#statusTo?.name()}, :comment)
            """)
    void create(
            long documentId, long initiatorId,
            DocumentAction action, String comment,
            DocumentStatus statusFrom, DocumentStatus statusTo
    );
}
