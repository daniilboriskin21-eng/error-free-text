package com.example.errorfreetext.task;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "text_correction_tasks")
public class TextCorrectionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_text", nullable = false, columnDefinition = "text")
    private String originalText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    @Column(name = "corrected_text", columnDefinition = "text")
    private String correctedText;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected TextCorrectionTask() {
    }

    public TextCorrectionTask(String originalText, Language language) {
        this.originalText = originalText;
        this.language = language;
        this.status = TaskStatus.NEW;
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public Language getLanguage() {
        return language;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getCorrectedText() {
        return correctedText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
