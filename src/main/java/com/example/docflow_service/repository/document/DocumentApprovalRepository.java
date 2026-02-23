package com.example.docflow_service.repository.document;

import com.example.docflow_service.entity.document_approval.DocumentApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DocumentApprovalRepository extends JpaRepository<DocumentApproval, Long> {
    @Modifying
    @Query(nativeQuery = true, value = """
            INSERT INTO document_approvals (document_id, initiator_id)
            VALUES (:documentId, :initiatorId)
            """)
    void create(long documentId, long initiatorId);


    long countByDocumentId(long documentId);
}
