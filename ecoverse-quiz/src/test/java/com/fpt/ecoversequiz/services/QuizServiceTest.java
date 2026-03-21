package com.fpt.ecoversequiz.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import com.fpt.ecoversequiz.dto.request.QuestionRequest;
import com.fpt.ecoversequiz.dto.request.QuizTemplateRequest;
import com.fpt.ecoversequiz.dto.request.SubmitQuizRequest;
import com.fpt.ecoversequiz.dto.response.QuizResultResponse;
import com.fpt.ecoversequiz.dto.response.QuizTemplateResponse;
import com.fpt.ecoversequiz.entities.Question;
import com.fpt.ecoversequiz.entities.QuizAttempt;
import com.fpt.ecoversequiz.entities.QuizTemplate;
import com.fpt.ecoversequiz.repositories.QuestionRepository;
import com.fpt.ecoversequiz.repositories.QuizAttemptRepository;
import com.fpt.ecoversequiz.repositories.QuizPlacementRepository;
import com.fpt.ecoversequiz.repositories.QuizTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizTemplateRepository templateRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizAttemptRepository attemptRepository;

    @Mock
    private QuizPlacementRepository placementRepository;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private QuizService quizService;

    private QuizTemplate sampleTemplate;
    private Question sampleQuestion;

    @BeforeEach
    void setUp() {
        sampleTemplate = new QuizTemplate();
        sampleTemplate.setId("qt001");
        sampleTemplate.setTitle("Quiz Phân loại rác cơ bản");
        sampleTemplate.setDescription("Kiểm tra kiến thức phân loại rác");
        sampleTemplate.setCreatedBy("PARTNERSHIP");
        sampleTemplate.setPartnerId("p001");
        sampleTemplate.setActive(true);

        sampleQuestion = new Question();
        sampleQuestion.setId("q001");
        sampleQuestion.setQuizTemplateId("qt001");
        sampleQuestion.setText("Lon nước ngọt thuộc loại rác nào?");
        sampleQuestion.setOptionsJson("[\"A. Hữu cơ\",\"B. Nhựa & Kim loại\",\"C. Giấy\",\"D. Khác\"]");
        sampleQuestion.setCorrectAnswer("B");
        sampleQuestion.setExplanation("Lon nhôm là kim loại, thuộc thùng nhựa & kim loại");
    }

    // ====================== TEMPLATE TESTS ======================

    @Test
    void getAllActiveTemplates_shouldReturnList() {
        when(templateRepository.findByActiveTrue()).thenReturn(Arrays.asList(sampleTemplate));
        when(questionRepository.countByQuizTemplateId("qt001")).thenReturn(1L);

        List<QuizTemplateResponse> result = quizService.getAllActiveTemplates();

        assertEquals(1, result.size());
        assertEquals("Quiz Phân loại rác cơ bản", result.get(0).getTitle());
        assertEquals(1, result.get(0).getQuestionCount());
    }

    @Test
    void getTemplateDetail_shouldReturnWithQuestions() {
        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(questionRepository.findByQuizTemplateId("qt001")).thenReturn(Arrays.asList(sampleQuestion));

        QuizTemplateResponse result = quizService.getTemplateDetail("qt001");

        assertNotNull(result.getQuestions());
        assertEquals(1, result.getQuestions().size());
        assertEquals("Lon nước ngọt thuộc loại rác nào?", result.getQuestions().get(0).getText());
    }

    @Test
    void getTemplateDetail_shouldThrowNotFound() {
        when(templateRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> quizService.getTemplateDetail("invalid"));
    }

    @Test
    void createTemplate_shouldSaveTemplateAndQuestions() {
        QuizTemplateRequest request = QuizTemplateRequest.builder()
                .title("Quiz mới")
                .description("Mô tả")
                .partnerId("p001")
                .questions(Arrays.asList(
                        QuestionRequest.builder()
                                .text("Câu hỏi 1")
                                .options(Arrays.asList("A. Sai", "B. Đúng"))
                                .correctAnswer("B")
                                .explanation("Giải thích")
                                .build()
                ))
                .build();

        when(templateRepository.save(any())).thenReturn(sampleTemplate);
        when(questionRepository.save(any())).thenReturn(sampleQuestion);

        QuizTemplateResponse result = quizService.createTemplate(request);

        assertNotNull(result);
        assertEquals("Quiz Phân loại rác cơ bản", result.getTitle());
        assertEquals("PARTNERSHIP", result.getCreatedBy()); // Always PARTNERSHIP
        verify(templateRepository, times(1)).save(any());
        verify(questionRepository, times(1)).save(any());
    }

    @Test
    void deleteTemplate_shouldSoftDelete() {
        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(templateRepository.save(any())).thenReturn(sampleTemplate);

        quizService.deleteTemplate("qt001");

        assertFalse(sampleTemplate.getActive());
        verify(templateRepository, times(1)).save(sampleTemplate);
    }

    // ====================== SUBMIT QUIZ TESTS ======================

    @Test
    void submitQuiz_shouldGradeCorrectly() {
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .studentId("s001")
                .quizTemplateId("qt001")
                .answers(Arrays.asList("B")) // correct answer
                .duration(120)
                .build();

        QuizAttempt savedAttempt = new QuizAttempt();
        savedAttempt.setId("a001");
        savedAttempt.setStudentId("s001");
        savedAttempt.setQuizTemplateId("qt001");
        savedAttempt.setScore(100);
        savedAttempt.setCorrectAmount(1);
        savedAttempt.setWrongAmount(0);
        savedAttempt.setAttemptNumber(1);
        savedAttempt.setCompleted(true);

        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(questionRepository.findByQuizTemplateId("qt001")).thenReturn(Arrays.asList(sampleQuestion));
        when(attemptRepository.countByQuizTemplateIdAndStudentId("qt001", "s001")).thenReturn(0L);
        when(attemptRepository.save(any())).thenReturn(savedAttempt);
        when(placementRepository.save(any())).thenReturn(null);

        QuizResultResponse result = quizService.submitQuiz(request);

        assertEquals(100, result.getScore());
        assertEquals(1, result.getCorrectAmount());
        assertEquals(0, result.getWrongAmount());
        assertTrue(result.getCompleted());
        assertEquals(1, result.getDetails().size());
        assertTrue(result.getDetails().get(0).getIsCorrect());
    }

    @Test
    void submitQuiz_shouldFailWhenAnswerCountMismatch() {
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .studentId("s001")
                .quizTemplateId("qt001")
                .answers(Arrays.asList("A", "B")) // 2 answers but only 1 question
                .duration(60)
                .build();

        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(questionRepository.findByQuizTemplateId("qt001")).thenReturn(Arrays.asList(sampleQuestion));

        assertThrows(BadRequestException.class, () -> quizService.submitQuiz(request));
    }

    @Test
    void submitQuiz_shouldThrowWhenNoQuestions() {
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .studentId("s001")
                .quizTemplateId("qt001")
                .answers(Arrays.asList("A"))
                .duration(60)
                .build();

        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(questionRepository.findByQuizTemplateId("qt001")).thenReturn(Arrays.asList()); // empty

        assertThrows(BadRequestException.class, () -> quizService.submitQuiz(request));
    }

    @Test
    void submitQuiz_shouldIncrementAttemptNumber() {
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .studentId("s001")
                .quizTemplateId("qt001")
                .answers(Arrays.asList("A")) // wrong answer
                .duration(90)
                .build();

        QuizAttempt savedAttempt = new QuizAttempt();
        savedAttempt.setId("a002");
        savedAttempt.setAttemptNumber(2); // second attempt
        savedAttempt.setScore(0);
        savedAttempt.setCorrectAmount(0);
        savedAttempt.setWrongAmount(1);
        savedAttempt.setCompleted(true);

        when(templateRepository.findById("qt001")).thenReturn(Optional.of(sampleTemplate));
        when(questionRepository.findByQuizTemplateId("qt001")).thenReturn(Arrays.asList(sampleQuestion));
        when(attemptRepository.countByQuizTemplateIdAndStudentId("qt001", "s001")).thenReturn(1L); // 1 previous
        when(attemptRepository.save(any())).thenReturn(savedAttempt);
        when(placementRepository.save(any())).thenReturn(null);

        QuizResultResponse result = quizService.submitQuiz(request);

        assertEquals(2, result.getAttemptNumber());
        assertEquals(0, result.getScore());
        assertFalse(result.getDetails().get(0).getIsCorrect());
    }
}
