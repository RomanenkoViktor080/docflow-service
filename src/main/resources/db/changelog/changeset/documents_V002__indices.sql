CREATE SEQUENCE document_number_seq
    START WITH 1
    INCREMENT BY 1;

-- documents
CREATE INDEX idx_documents_status_created_at ON documents (status, created_at DESC);

CREATE INDEX idx_documents_author_created_at ON documents (author_id, created_at DESC);

-- document_history
CREATE INDEX idx_document_history_document_created_at
    ON document_history (document_id, created_at DESC);

CREATE INDEX idx_document_history_initiator_created_at
    ON document_history (initiator_id, created_at DESC);