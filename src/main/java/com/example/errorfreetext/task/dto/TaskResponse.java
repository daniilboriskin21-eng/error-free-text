package com.example.errorfreetext.task.dto;

import com.example.errorfreetext.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskResponse(TaskStatus status, String correctedText, String errorMessage) {
}
