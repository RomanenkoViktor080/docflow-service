package com.example.docflow_service.entity.document_history;

import com.example.docflow_service.entity.document.Document;
import com.example.docflow_service.entity.document.DocumentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document_history")
public class DocumentHistory {

    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ToString.Include
    @Column(name = "initiator_id", nullable = false)
    private Long initiatorId;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private DocumentAction action;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private DocumentStatus fromStatus;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status")
    private DocumentStatus toStatus;

    @ToString.Include
    @Column(name = "comment")
    private String comment;

    @ToString.Include
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}