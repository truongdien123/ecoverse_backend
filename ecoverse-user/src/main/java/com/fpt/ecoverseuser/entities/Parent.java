package com.fpt.ecoverseuser.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parent extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    @Nationalized
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    @Nationalized
    private String address;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Partner partner;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Student> students;
}
