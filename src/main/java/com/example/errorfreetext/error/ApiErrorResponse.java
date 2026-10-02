package com.example.errorfreetext.error;

import java.time.OffsetDateTime;

public record ApiErrorResponse(String errorMessage, int errorCode, OffsetDateTime timestamp, String path) {
}
