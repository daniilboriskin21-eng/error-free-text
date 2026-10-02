package com.example.errorfreetext.task.dto;

import com.example.errorfreetext.task.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank
        @Size(min = 3)
        @Pattern(
                regexp = "(?s).*\\p{L}.*",
                message = "Text must contain at least one letter"
        )
        String text,

        @NotNull
        Language language
) {
}
