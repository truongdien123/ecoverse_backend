package com.fpt.ecoversequiz.repositories;

import com.fpt.ecoversequiz.entities.QuizTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizTemplateRepository extends JpaRepository<QuizTemplate, String> {
    List<QuizTemplate> findByActiveTrue();
    List<QuizTemplate> findByPartnerIdAndActiveTrue(String partnerId);
    boolean existsByTitleAndPartnerId(String title, String partnerId);
}
