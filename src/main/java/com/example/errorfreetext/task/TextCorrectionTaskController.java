package com.example.errorfreetext.task;

import com.example.errorfreetext.task.dto.CreateTaskRequest;
import com.example.errorfreetext.task.dto.CreateTaskResponse;
import com.example.errorfreetext.task.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TextCorrectionTaskController {

    private final TextCorrectionTaskService taskService;

    public TextCorrectionTaskController(TextCorrectionTaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        UUID id = taskService.createTask(request.text(), request.language());

        URI location = URI.create("/tasks/" + id);

        return ResponseEntity.created(location)
                .body(new CreateTaskResponse(id));
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable("id") UUID id) {
        TextCorrectionTask task = taskService.getTask(id);

        return switch (task.getStatus()) {
            case NEW, PROCESSING -> new TaskResponse(task.getStatus(), null, null);
            case COMPLETED -> new TaskResponse(task.getStatus(), task.getCorrectedText(), null);
            case ERROR -> new TaskResponse(task.getStatus(), null, task.getErrorMessage());
        };
    }
}
