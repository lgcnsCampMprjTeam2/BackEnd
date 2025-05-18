package com.lgcns.backend.csquestion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.dto.CSQuestionResponse;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;

@Service
public class CSQuestionService {
    @Autowired
    CSQuestionRepository csQuestionRepository;

    public List<CSQuestionResponse> getCSQuestionList(String categoryName) {
        List<CSQuestion> questions;

        // 카테고리 없는 경우 예외 처리 필요
        if (categoryName == null) {
            questions = csQuestionRepository.findAll();

        } else {
            Category category = Category.valueOf(categoryName);
            questions = csQuestionRepository.findByCategory(category);
        }

        return questions.stream()
                .map(q -> new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent()))
                .toList();
    }

    // 해당 Id의 cs질문 없는 경우 예외 처리 필요
    public CSQuestionResponse getCSQuestion(Long id) {
        Optional<CSQuestion> opt = csQuestionRepository.findById(id);
        if (!opt.isPresent()) {
            
        }

        CSQuestion q = opt.get();
        CSQuestionResponse res = new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent());

        return res;
    }
}
