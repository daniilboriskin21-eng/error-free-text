package com.example.errorfreetext.task.dto;

import com.example.errorfreetext.task.Language;

public record CreateTaskRequest(String text, Language language) {
}
