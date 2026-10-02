package com.example.errorfreetext.task.dto;

import com.example.errorfreetext.task.Language;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateTaskRequestTest {

    @Test
    void shouldAcceptValidRequest() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateTaskRequest("Привет, мир!", Language.RU);

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Test
    void shouldRejectTextWithoutLetters() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();

            var request = new CreateTaskRequest("123 !!!", Language.RU);

            var violations = validator.validate(request);

            assertEquals(1, violations.size());

            var violation = violations.iterator().next();

            assertEquals("text", violation.getPropertyPath().toString());

            assertEquals(Pattern.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    @Test
    void shouldRejectTooShortText() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();

            var request = new CreateTaskRequest("Hi", Language.EN);

            var violations = validator.validate(request);

            assertEquals(1, violations.size());

            var violation = violations.iterator().next();

            assertEquals("text", violation.getPropertyPath().toString());

            assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    @Test
    void shouldRejectMissingLanguage() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();

            var request = new CreateTaskRequest("Hello", null);

            var violations = validator.validate(request);

            assertEquals(1, violations.size());

            var violation = violations.iterator().next();

            assertEquals("language", violation.getPropertyPath().toString());

            assertEquals(NotNull.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    @Test
    void shouldRejectNullText() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateTaskRequest(null, Language.RU);

            var violations = validator.validate(request);

            assertEquals(1, violations.size());

            var violation = violations.iterator().next();

            assertEquals("text", violation.getPropertyPath().toString());

            assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        }
    }

    @Test
    void shouldAcceptExactlyThreeCharacters() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateTaskRequest("Hey", Language.EN);

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Test
    void shouldAcceptMultilineText() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateTaskRequest("123\nHello\n!!!", Language.EN);

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Test
    void shouldAcceptTextLongerThanApiLimit() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var request = new CreateTaskRequest("a".repeat(10001), Language.EN);

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }
}