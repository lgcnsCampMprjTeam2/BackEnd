package com.lgcns.backend.csquestion.repository;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSQuestiomRepository extends JpaRepository<CSQuestion, Long>{
    
}
