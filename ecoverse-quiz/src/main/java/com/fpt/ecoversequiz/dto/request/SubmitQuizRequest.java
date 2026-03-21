package com.fpt.ecoversequiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitQuizRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Quiz template ID is required")
    private String quizTemplateId;

    // Each answer maps to the question by index
    @NotEmpty(message = "Answers must not be empty")
    private List<String> answers; // ["A", "C", "B", ...]

    private Integer duration; // seconds taken
}
