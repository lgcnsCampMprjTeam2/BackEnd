package com.lgcns.backend.csanswer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.backend.csanswer.dto.CSAnswerRequest;
import com.lgcns.backend.csanswer.dto.CSAnswerResponse;
import com.lgcns.backend.csanswer.service.CSAnswerService;
import com.lgcns.backend.global.code.GeneralSuccessCode;
import com.lgcns.backend.global.response.CustomResponse;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/answer")
public class CSAnswerController {

    @Autowired
    private CSAnswerService csAnswerService;

    // 답변 작성
    @PostMapping
    public ResponseEntity<CustomResponse<CSAnswerResponse.CSAnswerDetailResponse>> createAnswer(
            @RequestBody CSAnswerRequest.CSAnswerCreateRequest request) {

        CSAnswerResponse.CSAnswerDetailResponse response = csAnswerService.createAnswer(request);
        System.out.println("응답 데이터: " + response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomResponse.created(response));
    }

    // 답변 리스트 조회
    @GetMapping
    public ResponseEntity<CustomResponse<List<CSAnswerResponse.CSAnswerListResponse>>> readAnswerList() {
        List<CSAnswerResponse.CSAnswerListResponse> responseList = csAnswerService.getAnswerList();
        return ResponseEntity.ok(CustomResponse.ok(responseList));
    }

    // 특정 답변 조회
    @GetMapping("/{answerId}")
    public ResponseEntity<CustomResponse<CSAnswerResponse.CSAnswerDetailResponse>> readAnswer(
            @PathVariable Long answerId) {
        CSAnswerResponse.CSAnswerDetailResponse response = csAnswerService.getAnswerDetail(answerId);
        return ResponseEntity.ok(CustomResponse.ok(response));
    }

    // 답변 수정
    @PostMapping("/{answerId}/edit")
    public ResponseEntity<CustomResponse<CSAnswerResponse.CSAnswerDetailResponse>> updateAnswer(
            @PathVariable Long answerId,
            @RequestBody CSAnswerRequest.CSAnswerUpdate request) {

        CSAnswerResponse.CSAnswerDetailResponse response = csAnswerService.updateAnswer(answerId, request);
        return ResponseEntity.ok(CustomResponse.ok(response));
    }

    // 답변 삭제
    @PostMapping("/{answerId}/delete")
    public ResponseEntity<CustomResponse<Void>> deleteAnswer(@PathVariable Long answerId) {
        csAnswerService.deleteAnswer(answerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(CustomResponse.success(GeneralSuccessCode._DELETED, null));
    }
}


