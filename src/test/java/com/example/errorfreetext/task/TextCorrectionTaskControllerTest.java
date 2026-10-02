package com.example.errorfreetext.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextCorrectionTaskController.class)
class TextCorrectionTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TextCorrectionTaskService taskService;

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        // Настраиваем сервис: при запросе этого UUID он выбросит исключение.
        when(taskService.getTask(id))
                .thenThrow(new TaskNotFoundException(id));

        // Отправляем запрос и проверяем HTTP-ответ.
        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(40401))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Task with id: " + id + " not found"))
                .andExpect(jsonPath("$.path").value("/tasks/" + id))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        // Проверяем, что контроллер обратился к сервису с нужным UUID.
        verify(taskService).getTask(id);
    }

    @Test
    void shouldReturn500WhenServiceFails() throws Exception {
        UUID id = UUID.randomUUID();

        when(taskService.getTask(id))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(50001))
                .andExpect(jsonPath("$.errorMessage").value("Internal server error"))
                .andExpect(jsonPath("$.path").value("/tasks/" + id))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verify(taskService).getTask(id);
    }

    @Test
    void shouldRejectInvalidRequestWithoutCallingService() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "text": "123 !!!",
                              "language": "EN"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40001))
                .andExpect(jsonPath("$.errorMessage")
                        .value("text: Text must contain at least one letter"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectInvalidUuidWithoutCallingService() throws Exception {
        mockMvc.perform(get("/tasks/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40003))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Task id must be a valid UUID"))
                .andExpect(jsonPath("$.path").value("/tasks/abc"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectUnknownLanguage() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "text": "Hello",
                              "language": "DE"
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40002))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Invalid JSON or field value. Language must be RU or EN"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectNumericLanguage() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "text": "Hello",
                              "language": 0
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40002))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Invalid JSON or field value. Language must be RU or EN"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "text": "Hello",
                              "language": "EN"
                            
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(40002))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Invalid JSON or field value. Language must be RU or EN"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectUnsupportedMethod() throws Exception {
        mockMvc.perform(delete("/tasks"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", "POST"))
                .andExpect(jsonPath("$.errorCode").value(40501))
                .andExpect(jsonPath("$.errorMessage")
                        .value("HTTP method is not supported for this endpoint"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }

    @Test
    void shouldRejectUnsupportedContentType() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Hello world"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.errorCode").value(41501))
                .andExpect(jsonPath("$.errorMessage")
                        .value("Unsupported content type. Use application/json"))
                .andExpect(jsonPath("$.path").value("/tasks"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verifyNoInteractions(taskService);
    }
}