package com.fpt.ecoverseuser.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    @Nationalized
    private String fullName;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "student_code", nullable = false, unique = true)
    private String studentCode;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Partner partner;
}
