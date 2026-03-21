package com.fpt.ecoversequiz.repositories;

import com.fpt.ecoversequiz.entities.QuizPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizPlacementRepository extends JpaRepository<QuizPlacement, String> {
    List<QuizPlacement> findByQuizAttemptId(String quizAttemptId);
    boolean existsByQuizAttemptIdAndQuestionId(String quizAttemptId, String questionId);
}
