package com.example.docflow_service.entity.document;

import com.example.docflow_service.entity.document_history.DocumentHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ToString.Include
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @ToString.Include
    @Column(name = "title", nullable = false)
    private String title;

    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentStatus status;

    @OneToMany(mappedBy = "document")
    private List<DocumentHistory> history;

    @ToString.Include
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Include
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}