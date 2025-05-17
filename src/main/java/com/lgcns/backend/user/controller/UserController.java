package com.lgcns.backend.user.controller;

import com.lgcns.backend.user.dto.request.SignUpRequestDto;
import com.lgcns.backend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 회원가입
    @PostMapping("/user/signup")
    public void signUp(@ResponseBody SignUpRequestDto dto) {
        try {
            String message = UserService.signUp(dto);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
