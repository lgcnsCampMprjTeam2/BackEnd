package com.lgcns.backend.csanswer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lgcns.backend.csanswer.domain.CSAnswer;
import com.lgcns.backend.csanswer.dto.CSAnswerRequest;
import com.lgcns.backend.csanswer.dto.CSAnswerResponse;
import com.lgcns.backend.csanswer.repository.CSAnswerRepository;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;

@Service
public class CSAnswerService {

    @Autowired
    private CSAnswerRepository csAnswerRepository;

    @Autowired
    private CSQuestionRepository csQuestionRepository;

    @Autowired
    private UserRepository userRepository;

    public CSAnswerResponse.CSAnswerDetailResponse createAnswer(CSAnswerRequest.CSAnswerCreateRequest request, UserDetails userDetails) {
        CSQuestion question = csQuestionRepository.findById(request.getCsquestion_id())
                .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

        // 로그인 기능 구현 전으로, 하드코딩
        User user1 = getCurrentUser();
        User user = getUserFromDetails(userDetails);


        CSAnswer answer = new CSAnswer();
        answer.setContent(request.getCsanswer_content());
        answer.setCreatedAt(LocalDateTime.now());
        answer.setFeedback("아직 피드백 없음");
        answer.setCsQuestion(question);
        answer.setUser(user);

        csAnswerRepository.save(answer);

        return CSAnswerResponse.CSAnswerDetailResponse.builder()
                .csquestion_id(question.getId())
                .csquestion_category(question.getCategory())
                .csquestion_content(question.getContent())
                .csanswer_id(answer.getId())
                .csanswer_content(answer.getContent())
                .csanswer_created_at(answer.getCreatedAt())
                .csanswer_feedback(answer.getFeedback())
                .build();
    }

    public List<CSAnswerResponse.CSAnswerListResponse> getAnswerList(UserDetails userDetails) {
        // 로그인 기능 구현 전으로, 하드코딩
        User user1 = getCurrentUser();
        User user = getUserFromDetails(userDetails);
        
        List<CSAnswer> answers = csAnswerRepository.findAllByUser(user);

        return answers.stream().map(answer -> {
            CSQuestion question = answer.getCsQuestion();
            return CSAnswerResponse.CSAnswerListResponse.builder()
                    .user_nickname(answer.getUser().getNickname())
                    .csquestion_id(question.getId())
                    .csquestion_category(question.getCategory())
                    .csquestion_content(question.getContent())
                    .csanswer_id(answer.getId())
                    .csanswer_content(answer.getContent())
                    .csanswer_created_at(answer.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    public CSAnswerResponse.CSAnswerDetailResponse getAnswerDetail(Long answerId, UserDetails userDetails) {
        // 로그인 기능 구현 전으로, 하드코딩
        User user1 = getCurrentUser();
        User user = getUserFromDetails(userDetails);

        CSAnswer answer = csAnswerRepository.findById(answerId) //400
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));
        
        CSQuestion question = answer.getCsQuestion();

        if (!answer.getUser().getId().equals(user.getId())) { //500
            throw new RuntimeException("자신의 답변만 조회할 수 있습니다.");
        }

        return CSAnswerResponse.CSAnswerDetailResponse.builder()
                .csquestion_id(question.getId())
                .csquestion_category(question.getCategory())
                .csquestion_content(question.getContent())
                .csanswer_id(answer.getId())
                .csanswer_content(answer.getContent())
                .csanswer_created_at(answer.getCreatedAt())
                .csanswer_feedback(answer.getFeedback())
                .build();
    }

    public CSAnswerResponse.CSAnswerDetailResponse updateAnswer(Long answerId, CSAnswerRequest.CSAnswerUpdate request, UserDetails userDetails) {
        // 로그인 기능 구현 전으로, 하드코딩
        User user1 = getCurrentUser();
        User user = getUserFromDetails(userDetails);
        
        CSAnswer answer = csAnswerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));

        if (!answer.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("자신의 답변만 수정할 수 있습니다.");
        }

        answer.setContent(request.getCsanswer_content());
        answer.setFeedback("아직 피드백 없음");
        csAnswerRepository.save(answer);

        CSQuestion question = answer.getCsQuestion();
        return CSAnswerResponse.CSAnswerDetailResponse.builder()
                .csquestion_id(question.getId())
                .csquestion_category(question.getCategory())
                .csquestion_content(question.getContent())
                .csanswer_id(answer.getId())
                .csanswer_content(answer.getContent())
                .csanswer_created_at(answer.getCreatedAt())
                .csanswer_feedback(answer.getFeedback())
                .build();
    }

    public void deleteAnswer(Long answerId, UserDetails userDetails) {
        // 로그인 기능 구현 전으로, 하드코딩
        User user1 = getCurrentUser();
        User user = getUserFromDetails(userDetails);
        CSAnswer answer = csAnswerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("답변이 존재하지 않습니다."));

        if (!answer.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("자신의 답변만 삭제할 수 있습니다.");
        }
        csAnswerRepository.deleteById(answerId);
    }

    private User getCurrentUser() {
    return userRepository.findById(1L)
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }

    private User getUserFromDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }

}
