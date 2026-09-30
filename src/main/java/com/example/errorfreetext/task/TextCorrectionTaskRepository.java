package com.example.errorfreetext.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TextCorrectionTaskRepository
        extends JpaRepository<TextCorrectionTask, UUID> {
}
