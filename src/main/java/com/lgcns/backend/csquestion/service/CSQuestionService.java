package com.lgcns.backend.csquestion.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lgcns.backend.csanswer.domain.CSAnswer;
import com.lgcns.backend.csanswer.repository.CSAnswerRepository;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.dto.CSQuestionResponse;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;

@Service
public class CSQuestionService {
    @Autowired
    CSQuestionRepository csQuestionRepository;

    @Autowired
    CSAnswerRepository csAnswerRepository;

    @Autowired
    UserRepository userRepository;

    public Page<CSQuestionResponse> getCSQuestionList(String categoryName, Pageable pageable, UserDetails userDetails) {
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

        // 제출 정보
        User user = getUserFromDetails(userDetails);
        List<CSAnswer> answers = csAnswerRepository.findAllByUser(user);
        Set<Long> submittedQuestionsIds = answers.stream().map((a)->a.getCsQuestion().getId()).collect(Collectors.toSet());


        return questions.map(q -> new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent(), submittedQuestionsIds.contains(q.getId())));
    }

    public CSQuestionResponse getCSQuestion(Long id) {
        CSQuestion q = csQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        CSQuestionResponse res = new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent(), false);

        return res;
    }

    public CSQuestionResponse getTodayCSQuestion() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        CSQuestion q = csQuestionRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        if (q == null) {
            throw new NoSuchElementException("오늘의 질문이 아직 등록되지 않았습니다.");
        }

        CSQuestionResponse res = new CSQuestionResponse(q.getId(), q.getCategory(), q.getCreatedAt(), q.getContent(), false);

        return res;
    }

    public void deleteQuestion(Long id) {
        CSQuestion q = csQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        csQuestionRepository.delete(q);
    }

    private User getUserFromDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }
}
