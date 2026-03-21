package com.fpt.ecoversequiz.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_placements",
        indexes = {
                @Index(columnList = "quiz_attempt_id"),
                @Index(columnList = "question_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"quiz_attempt_id", "question_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizPlacement extends BaseEntity {

    @Column(name = "quiz_attempt_id", nullable = false, length = 36)
    private String quizAttemptId;

    @Column(name = "question_id", nullable = false, length = 36)
    private String questionId;

    @Column(name = "selected_answer", length = 255)
    private String selectedAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;
}
