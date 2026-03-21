package com.fpt.ecoversequiz.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResultResponse {
    private String attemptId;
    private String quizTemplateId;
    private String studentId;
    private Integer score;
    private Integer correctAmount;
    private Integer wrongAmount;
    private Integer totalQuestions;
    private Integer duration; // seconds
    private Integer attemptNumber;
    private Boolean completed;
    private List<PlacementDetail> details;
    private LocalDateTime completedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlacementDetail {
        private String questionId;
        private String questionText;
        private List<String> options;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
        private String explanation;
    }
}
