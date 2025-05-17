package com.lgcns.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.backend.entity.CSQuestion;

public interface CSQuestiomRepository extends JpaRepository<CSQuestion, Long>{
    
}
