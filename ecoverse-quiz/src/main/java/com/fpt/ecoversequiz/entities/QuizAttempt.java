package com.fpt.ecoversequiz.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_attempts", indexes = {
        @Index(name = "idx_quiz_attempts_template_id", columnList = "quiz_template_id"),
        @Index(name = "idx_quiz_attempts_student_id", columnList = "student_id"),
        @Index(name = "idx_quiz_attempts_template_student", columnList = "quiz_template_id,student_id"),
        @Index(name = "idx_quiz_attempts_completed", columnList = "completed")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt extends BaseEntity {

    @Column(name = "quiz_template_id", nullable = false, length = 36)
    private String quizTemplateId;

    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;

    // JSON string: ["A","C","B",...]
    @Column(name = "selected_answers_json", columnDefinition = "TEXT")
    private String selectedAnswersJson;

    @Column(name = "score")
    private Integer score = 0;

    @Column(name = "correct_amount")
    private Integer correctAmount;

    @Column(name = "wrong_amount")
    private Integer wrongAmount;

    @Column(name = "completed")
    private Boolean completed = false;

    @Column(name = "duration")
    private Integer duration; // seconds

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;
}
