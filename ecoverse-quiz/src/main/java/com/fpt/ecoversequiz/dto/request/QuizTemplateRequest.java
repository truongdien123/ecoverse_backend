package com.fpt.ecoversequiz.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizTemplateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Partner ID is required")
    private String partnerId;

    @NotEmpty(message = "Questions list must not be empty")
    @Valid
    private List<QuestionRequest> questions;
}
