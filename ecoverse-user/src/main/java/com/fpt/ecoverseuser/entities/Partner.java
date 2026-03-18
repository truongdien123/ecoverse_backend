package com.fpt.ecoverseuser.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.ecoversecommon.entity.BaseEntity;
import com.fpt.ecoverseuser.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "partnerships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partner extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "organization_name", nullable = false)
    @Nationalized
    private String organizationName;

    @Column(name = "address", nullable = false)
    @Nationalized
    private String address;

    @Column(name = "contact_person", nullable = false)
    @Nationalized
    private String contactPerson;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.PENDING;

    @OneToMany(mappedBy = "partner")
    @JsonIgnore
    private List<Parent> parents;

    @OneToMany(mappedBy = "partner")
    @JsonIgnore
    private List<Student> students;
}
