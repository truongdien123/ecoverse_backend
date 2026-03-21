package com.fpt.ecoversequiz.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoversequiz.dto.request.QuestionRequest;
import com.fpt.ecoversequiz.dto.request.QuizTemplateRequest;
import com.fpt.ecoversequiz.dto.request.SubmitQuizRequest;
import com.fpt.ecoversequiz.dto.response.QuizResultResponse;
import com.fpt.ecoversequiz.dto.response.QuizTemplateResponse;
import com.fpt.ecoversequiz.services.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuizTemplateResponse sampleTemplateResponse;
    private QuizResultResponse sampleResultResponse;

    @BeforeEach
    void setUp() {
        sampleTemplateResponse = QuizTemplateResponse.builder()
                .id("qt001")
                .title("Quiz Phân loại rác cơ bản")
                .description("Kiểm tra kiến thức phân loại rác")
                .createdBy("ADMIN")
                .partnerId("p001")
                .active(true)
                .questionCount(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleResultResponse = QuizResultResponse.builder()
                .attemptId("a001")
                .quizTemplateId("qt001")
                .studentId("s001")
                .score(100)
                .correctAmount(2)
                .wrongAmount(0)
                .totalQuestions(2)
                .duration(120)
                .attemptNumber(1)
                .completed(true)
                .details(Arrays.asList())
                .completedAt(LocalDateTime.now())
                .build();
    }

    // ====================== TEMPLATE TESTS ======================

    @Test
    void getAllTemplates_shouldReturnList() throws Exception {
        when(quizService.getAllActiveTemplates()).thenReturn(Arrays.asList(sampleTemplateResponse));

        mockMvc.perform(get("/api/quiz/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Quiz Phân loại rác cơ bản"))
                .andExpect(jsonPath("$.data[0].questionCount").value(2));
    }

    @Test
    void getTemplateDetail_shouldReturnDetail() throws Exception {
        when(quizService.getTemplateDetail(anyString())).thenReturn(sampleTemplateResponse);

        mockMvc.perform(get("/api/quiz/templates/qt001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("qt001"))
                .andExpect(jsonPath("$.data.title").value("Quiz Phân loại rác cơ bản"));
    }

    @Test
    void getByPartner_shouldReturnPartnerTemplates() throws Exception {
        when(quizService.getTemplatesByPartner(anyString())).thenReturn(Arrays.asList(sampleTemplateResponse));

        mockMvc.perform(get("/api/quiz/templates/partner/p001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].partnerId").value("p001"));
    }

    @Test
    void createTemplate_shouldReturnCreated() throws Exception {
        QuizTemplateRequest request = QuizTemplateRequest.builder()
                .title("Quiz Phân loại rác cơ bản")
                .description("Kiểm tra kiến thức")
                .partnerId("p001")
                .questions(Arrays.asList(
                        QuestionRequest.builder()
                                .text("Câu hỏi 1")
                                .options(Arrays.asList("A. Sai", "B. Đúng"))
                                .correctAnswer("B")
                                .build()
                ))
                .build();

        when(quizService.createTemplate(any())).thenReturn(sampleTemplateResponse);

        mockMvc.perform(post("/api/quiz/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Quiz Phân loại rác cơ bản"));
    }

    @Test
    void updateTemplate_shouldReturnUpdated() throws Exception {
        QuizTemplateRequest request = QuizTemplateRequest.builder()
                .title("Quiz Cập nhật")
                .partnerId("p001")
                .questions(Arrays.asList(
                        QuestionRequest.builder()
                                .text("Câu hỏi mới")
                                .options(Arrays.asList("A. Sai", "B. Đúng"))
                                .correctAnswer("B")
                                .build()
                ))
                .build();

        when(quizService.updateTemplate(anyString(), any())).thenReturn(sampleTemplateResponse);

        mockMvc.perform(put("/api/quiz/templates/qt001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteTemplate_shouldReturnOk() throws Exception {
        doNothing().when(quizService).deleteTemplate(anyString());

        mockMvc.perform(delete("/api/quiz/templates/qt001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ====================== SUBMIT AND HISTORY TESTS ======================

    @Test
    void submitQuiz_shouldReturnResult() throws Exception {
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .studentId("s001")
                .quizTemplateId("qt001")
                .answers(Arrays.asList("B", "A"))
                .duration(120)
                .build();

        when(quizService.submitQuiz(any())).thenReturn(sampleResultResponse);

        mockMvc.perform(post("/api/quiz/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.score").value(100))
                .andExpect(jsonPath("$.data.correctAmount").value(2))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void getStudentHistory_shouldReturnHistory() throws Exception {
        List<QuizResultResponse> history = Arrays.asList(sampleResultResponse);
        when(quizService.getStudentHistory(anyString())).thenReturn(history);

        mockMvc.perform(get("/api/quiz/history/s001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].studentId").value("s001"))
                .andExpect(jsonPath("$.data[0].score").value(100));
    }

    @Test
    void createTemplate_shouldReturn400_whenMissingTitle() throws Exception {
        QuizTemplateRequest request = QuizTemplateRequest.builder()
                .title("") // blank title → validation fail
                .questions(Arrays.asList(
                        QuestionRequest.builder()
                                .text("Câu hỏi")
                                .options(Arrays.asList("A", "B"))
                                .correctAnswer("A")
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/quiz/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
