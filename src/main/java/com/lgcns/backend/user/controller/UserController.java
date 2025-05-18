package com.lgcns.backend.user.controller;

import com.lgcns.backend.global.code.GeneralErrorCode;
import com.lgcns.backend.global.response.CustomResponse;
import com.lgcns.backend.security.util.JwtUtil;
import com.lgcns.backend.user.dto.request.LoginRequestDto;
import com.lgcns.backend.user.dto.request.SignUpRequestDto;
import com.lgcns.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authManager;

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

    @PostMapping("/user/login")
    public ResponseEntity<CustomResponse<String>> login(@RequestBody LoginRequestDto dto) {
        CustomResponse<String> response = userService.login(dto);
        
        // 리턴이 실패인 경우
        if (!response.isSuccess()) {
            return ResponseEntity.status(GeneralErrorCode._UNAUTHORIZED.getHttpStatus())
                    .body(response);
        }
        
        // 성공인 경우
        return ResponseEntity.ok(response);
    }

}
