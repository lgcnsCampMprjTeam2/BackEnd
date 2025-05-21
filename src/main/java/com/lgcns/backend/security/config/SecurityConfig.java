package com.lgcns.backend.security.config;

import com.lgcns.backend.security.filter.JwtAuthFilter;
import com.lgcns.backend.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtUtil jwtUtil,
                                                   UserDetailsService userDetailsService) throws Exception {
        http
                // csrf 비활성화
                .csrf(csrf -> {
                    csrf.disable();
                })
                // 경로멸 인가
                .authorizeHttpRequests(auth -> auth
                        // 회원가입, 로그인 모든 권한 인가
                        .requestMatchers("/user/signup").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/api/questions").permitAll()
                        .requestMatchers("/profile").permitAll()
                        .anyRequest().authenticated())
                // JWT 커스텀 사용자 인증 필터 등록
                .addFilterBefore(
                        new JwtAuthFilter(jwtUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                // cors 설정
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            // 서버 접근을 허용할 출처 목록, "*"로 설정시, 모든 origin 가능
                            config.setAllowedOrigins(List.of("http://localhost:3000"));
                            config.setAllowedMethods(List.of("GET", "POST"));
                            config.setAllowCredentials(true);
                            config.setAllowedHeaders(List.of("*"));
                            return config;
                        }));
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                            response.setContentType("application/json; charset=UTF-8");
//                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
//                        }));

        return http.build();
    }


    // 비밀번호 암호화 기능
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }


}
