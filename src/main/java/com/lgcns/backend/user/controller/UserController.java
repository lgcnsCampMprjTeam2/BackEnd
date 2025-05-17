package com.lgcns.backend.user.controller;

import com.lgcns.backend.global.code.GeneralErrorCode;
import com.lgcns.backend.global.response.CustomResponse;
import com.lgcns.backend.user.dto.request.SignUpRequestDto;
import com.lgcns.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<CustomResponse<String>> signUp(@RequestBody SignUpRequestDto dto) {

        try {
            userService.signUp(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(CustomResponse.created("회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(GeneralErrorCode._EMAIL_USED.getHttpStatus())
                    .body(CustomResponse.fail(GeneralErrorCode._EMAIL_USED, "이미 존재하는 이메일입니다."));
        }
    }

}
