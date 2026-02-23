CREATE TABLE documents
(
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    number     VARCHAR(255) NOT NULL UNIQUE,
    author_id  BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    status     VARCHAR(128) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT current_timestamp
);

CREATE TABLE document_history
(
    id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    document_id  BIGINT       NOT NULL REFERENCES documents (id) ON DELETE CASCADE,
    initiator_id BIGINT       NOT NULL,
    action       VARCHAR(128) NOT NULL,
    from_status  VARCHAR(128),
    to_status    VARCHAR(128),
    comment      VARCHAR(255),
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT current_timestamp
);

CREATE TABLE document_approvals
(
    id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    document_id  BIGINT      NOT NULL UNIQUE REFERENCES documents (id) ON DELETE CASCADE,
    initiator_id BIGINT      NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT current_timestamp
);
