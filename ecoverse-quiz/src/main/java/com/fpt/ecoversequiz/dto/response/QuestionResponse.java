package com.fpt.ecoversequiz.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private String id;
    private String quizTemplateId;
    private String text;
    private List<String> options; // deserialized from options_json
    private String correctAnswer;  // only shown in result, hidden during quiz
    private String explanation;
}
