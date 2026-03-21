package com.fpt.ecoversequiz.repositories;

import com.fpt.ecoversequiz.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByQuizTemplateId(String quizTemplateId);
    void deleteByQuizTemplateId(String quizTemplateId);
    long countByQuizTemplateId(String quizTemplateId);
}
