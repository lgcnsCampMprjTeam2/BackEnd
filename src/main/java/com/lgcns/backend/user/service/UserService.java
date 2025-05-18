package com.lgcns.backend.user.service;


import com.lgcns.backend.global.code.GeneralErrorCode;
import com.lgcns.backend.global.code.GeneralSuccessCode;
import com.lgcns.backend.global.response.CustomResponse;
import com.lgcns.backend.security.util.JwtUtil;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.dto.request.LoginRequestDto;
import com.lgcns.backend.user.dto.request.SignUpRequestDto;
import com.lgcns.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // user레파지, PasswordEncoder, jwt유틸 와이어링
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authManager;


    public void signUp(SignUpRequestDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.getEmail());

        if (user.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setName(dto.getName());
        newUser.setNickname(dto.getNickname());
        newUser.setProfileImage(dto.getProfileImage());
        // userRole을 User로 지정
        newUser.setRole("ROLE_USER");


        userRepository.save(newUser);

    }

    public CustomResponse login(LoginRequestDto dto) {
        // 1. 일일히 DB와 대조하는 방식
//        Optional<User> user = userRepository.findByEmail(dto.getEmail());
//
//        if (user.isEmpty()) {
//            return CustomResponse
//                    .fail(GeneralErrorCode._NOT_FOUND,
//                            "회원 정보가 존재하지 않습니다.");
//        }
//
//        User currentUser = user.get();
//        if (!passwordEncoder.matches(dto.getPassword(), currentUser.getPassword())) {
//            return CustomResponse
//                    .fail(GeneralErrorCode._UNAUTHORIZED,
//                            "비밀번호가 일치하지 않습니다.");
//        }
//
//        // JWT 발급
//        return CustomResponse.success();
        try {
            // 이메일을 username으로 취급함
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(), dto.getPassword()));

            // 인증 성공시 JWT 발행
            String token = jwtUtil.createAccessToken(authentication.getName());
            System.out.println("✅ 인증 성공: " + authentication.getName());
            return CustomResponse.success(GeneralSuccessCode._OK, token);

        } catch (AuthenticationException e) {
            System.out.println("❌ 인증 실패: " + e.getMessage());
            return CustomResponse.fail(GeneralErrorCode._UNAUTHORIZED, "로그인 실패: 잘못된 이메일 또는 비밀번호");

        }

    }
}
