package com.fpt.ecoversequiz.controllers;

import com.fpt.ecoversequiz.dto.request.QuizTemplateRequest;
import com.fpt.ecoversequiz.dto.request.SubmitQuizRequest;
import com.fpt.ecoversequiz.dto.response.QuizResultResponse;
import com.fpt.ecoversequiz.dto.response.QuizTemplateResponse;
import com.fpt.ecoversequiz.services.QuizService;
import com.fpt.ecoversecommon.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // ====================== QUIZ TEMPLATE ======================

    /**
     * POST /api/quiz/templates
     * Create a new quiz template with questions (PARTNERSHIP/ADMIN)
     */
    @PostMapping("/templates")
    public ResponseEntity<ApiResponse<QuizTemplateResponse>> createTemplate(
            @Valid @RequestBody QuizTemplateRequest request) {
        QuizTemplateResponse response = quizService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Quiz template created successfully", response));
    }

    /**
     * GET /api/quiz/templates
     * Get all active quiz templates
     */
    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<QuizTemplateResponse>>> getAllTemplates() {
        List<QuizTemplateResponse> list = quizService.getAllActiveTemplates();
        return ResponseEntity.ok(ApiResponse.success("Quiz templates retrieved", list));
    }

    /**
     * GET /api/quiz/templates/partner/{partnerId}
     * Get quiz templates by partner (school)
     */
    @GetMapping("/templates/partner/{partnerId}")
    public ResponseEntity<ApiResponse<List<QuizTemplateResponse>>> getByPartner(
            @PathVariable("partnerId") String partnerId) {
        List<QuizTemplateResponse> list = quizService.getTemplatesByPartner(partnerId);
        return ResponseEntity.ok(ApiResponse.success("Templates retrieved", list));
    }

    /**
     * GET /api/quiz/templates/{id}
     * Get quiz template detail with all questions
     */
    @GetMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<QuizTemplateResponse>> getTemplateDetail(
            @PathVariable("id") String id) {
        QuizTemplateResponse response = quizService.getTemplateDetail(id);
        return ResponseEntity.ok(ApiResponse.success("Template detail retrieved", response));
    }

    /**
     * PUT /api/quiz/templates/{id}
     * Update quiz template and replace questions (PARTNERSHIP/ADMIN)
     */
    @PutMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<QuizTemplateResponse>> updateTemplate(
            @PathVariable("id") String id,
            @Valid @RequestBody QuizTemplateRequest request) {
        QuizTemplateResponse response = quizService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success("Quiz template updated", response));
    }

    /**
     * DELETE /api/quiz/templates/{id}
     * Soft delete quiz template (PARTNERSHIP/ADMIN)
     */
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable("id") String id) {
        quizService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Quiz template deleted", null));
    }

    // ====================== QUIZ ATTEMPT (STUDENT) ======================

    /**
     * POST /api/quiz/submit
     * Submit quiz answers and get result (STUDENT)
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<QuizResultResponse>> submitQuiz(
            @Valid @RequestBody SubmitQuizRequest request) {
        QuizResultResponse result = quizService.submitQuiz(request);
        return ResponseEntity.ok(ApiResponse.success("Quiz submitted successfully", result));
    }

    /**
     * GET /api/quiz/history/{studentId}
     * Get quiz history of a student
     */
    @GetMapping("/history/{studentId}")
    public ResponseEntity<ApiResponse<List<QuizResultResponse>>> getStudentHistory(
            @PathVariable("studentId") String studentId) {
        List<QuizResultResponse> history = quizService.getStudentHistory(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student quiz history retrieved", history));
    }
}
