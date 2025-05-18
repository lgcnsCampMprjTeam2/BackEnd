package com.lgcns.backend.csquestion.repository;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.lgcns.backend.global.domain.Category;
import java.time.LocalDateTime;



public interface CSQuestionRepository extends JpaRepository<CSQuestion, Long>{
    List<CSQuestion> findByCategory(Category category);
    CSQuestion findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
