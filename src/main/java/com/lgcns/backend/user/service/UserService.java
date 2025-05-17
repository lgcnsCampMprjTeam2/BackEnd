package com.lgcns.backend.user.service;


import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.dto.request.SignUpRequestDto;
import com.lgcns.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // user레파지, PasswordEncoder 와이어링
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    public String signUp(SignUpRequestDto dto) {
        Optional<User> user = userRepository.findByUsername(dto.getEmail());

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

        userRepository.save(newUser);

        return "회원가입 성공";
    }
}
