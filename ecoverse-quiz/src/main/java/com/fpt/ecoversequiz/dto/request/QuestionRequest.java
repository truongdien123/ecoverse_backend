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
public class QuestionRequest {

    @NotBlank(message = "Question text is required")
    private String text;

    @NotEmpty(message = "Options must not be empty")
    private List<String> options; // Will be serialized to JSON

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    private String explanation;
}
