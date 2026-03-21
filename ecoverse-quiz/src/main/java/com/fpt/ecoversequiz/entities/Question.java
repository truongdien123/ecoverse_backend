package com.fpt.ecoversequiz.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions", indexes = {
        @Index(name = "idx_questions_template_id", columnList = "quiz_template_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseEntity {

    @Column(name = "quiz_template_id", nullable = false, length = 36)
    private String quizTemplateId;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    // JSON string: ["A. Nhựa","B. Giấy","C. Hữu cơ","D. Khác"]
    @Column(name = "options_json", columnDefinition = "TEXT")
    private String optionsJson;

    @Column(name = "correct_answer", nullable = false, length = 255)
    private String correctAnswer;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;
}
