package com.fpt.ecoversequiz.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_templates", indexes = {
        @Index(name = "idx_quiz_templates_partner_id", columnList = "partner_id"),
        @Index(name = "idx_quiz_templates_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizTemplate extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", length = 50)
    private String createdBy; // PARTNERSHIP | ADMIN

    @Column(name = "partner_id", length = 36)
    private String partnerId;

    @Column(name = "active")
    private Boolean active = true;
}
