package com.example.errorfreetext.task;

import org.springframework.stereotype.Service;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

@Service
public class TextCorrectionTaskService {
    private final TextCorrectionTaskRepository taskRepository;

    public TextCorrectionTaskService(TextCorrectionTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public UUID createTask(String text, Language language) {
        TextCorrectionTask task = new TextCorrectionTask(text, language);

        TextCorrectionTask savedTask = taskRepository.save(task);

        return savedTask.getId();
    }

    @Transactional(readOnly = true)
    public TextCorrectionTask getTask(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
