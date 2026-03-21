package com.fpt.ecoversequiz.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizTemplateResponse {
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String partnerId;
    private Boolean active;
    private int questionCount;
    private List<QuestionResponse> questions; // null when listing, populated when detail
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
