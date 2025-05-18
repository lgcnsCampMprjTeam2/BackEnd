package com.lgcns.backend.csquestion.controller;

import org.springframework.web.bind.annotation.RestController;

import com.lgcns.backend.csquestion.dto.CSQuestionResponse;
import com.lgcns.backend.csquestion.service.CSQuestionService;
import com.lgcns.backend.global.response.CustomResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/questions")
public class CSQuestionController {

    @Autowired CSQuestionService csQuestionService;
    
    @GetMapping
    public ResponseEntity<?> getCSQuestionList(@RequestParam(required = false) String category) {
        List<CSQuestionResponse> list = csQuestionService.getCSQuestionList(category);
        return ResponseEntity.ok(CustomResponse.ok(list));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getCSQuestion(@PathVariable Long questionId) {
        CSQuestionResponse question = csQuestionService.getCSQuestion(questionId);
        
        return ResponseEntity.ok(CustomResponse.ok(question));
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayCSQuestion() {
        CSQuestionResponse question = csQuestionService.getTodayCSQuestion();

        return ResponseEntity.ok(CustomResponse.ok(question));
    }
}
