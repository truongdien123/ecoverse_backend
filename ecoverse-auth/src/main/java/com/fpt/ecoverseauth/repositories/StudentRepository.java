package com.fpt.ecoverseauth.repositories;

import com.fpt.ecoverseuser.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("authStudentRepository")
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByStudentCode(String studentCode);
    boolean existsByStudentCode(String studentCode);
}
