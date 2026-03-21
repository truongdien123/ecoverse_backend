package com.fpt.ecoversequiz.repositories;

import com.fpt.ecoversequiz.entities.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, String> {
    List<QuizAttempt> findByStudentId(String studentId);
    List<QuizAttempt> findByQuizTemplateIdAndStudentId(String quizTemplateId, String studentId);
    Optional<QuizAttempt> findTopByQuizTemplateIdAndStudentIdOrderByAttemptNumberDesc(String quizTemplateId, String studentId);
    long countByQuizTemplateIdAndStudentId(String quizTemplateId, String studentId);
}
