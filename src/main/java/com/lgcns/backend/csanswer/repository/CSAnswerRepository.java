package com.lgcns.backend.csanswer.repository;

import com.lgcns.backend.csanswer.domain.CSAnswer;
import com.lgcns.backend.user.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CSAnswerRepository extends JpaRepository<CSAnswer, Long>{
    List<CSAnswer> findAllByUser(User user);
}
