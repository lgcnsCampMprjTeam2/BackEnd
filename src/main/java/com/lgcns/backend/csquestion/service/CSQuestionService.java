package com.lgcns.backend.csquestion.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.dto.CSQuestionResponse;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;

@Service
public class CSQuestionService {
    @Autowired
    CSQuestionRepository csQuestionRepository;

    public Page<CSQuestionResponse> getCSQuestionList(String categoryName, Pageable pageable) {
        Page<CSQuestion> questions;

        if (categoryName == null) {
            questions = csQuestionRepository.findAll(pageable);

        } else {
            try {
                Category category = Category.valueOf(categoryName);
                questions = csQuestionRepository.findByCategory(category, pageable);
            } catch (IllegalArgumentException e) {
                questions = csQuestionRepository.findAll(pageable);
            }
        }

        return questions.map(q -> new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent()));
    }

    public CSQuestionResponse getCSQuestion(Long id) {
        CSQuestion q = csQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        CSQuestionResponse res = new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent());

        return res;
    }

    public CSQuestionResponse getTodayCSQuestion() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        CSQuestion q = csQuestionRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        if(q == null){
            throw new NoSuchElementException("오늘의 질문이 아직 등록되지 않았습니다.");
        }

        CSQuestionResponse res = new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent());

        return res;
    }

    public void deleteQuestion(Long id) {
        CSQuestion q = csQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        csQuestionRepository.delete(q);
    }
}
