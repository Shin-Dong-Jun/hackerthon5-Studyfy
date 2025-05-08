package org.example.studyfy.config;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.filter.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//security 설정
@Configuration  //spring 설정 클래스
@EnableWebSecurity //Spring Security 활성화 및 보안 설정 적용
@RequiredArgsConstructor
public class SecurityConfig {
    private  final JwtTokenFilter jwtTokenFilter;

    //SecurityFilterChain : Spring Security의 핵심 보안 체인
    //HttpSecurity : Http 요청에 대해 보안 설정을 정의 하는 객체
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception{
        return http
                .csrf(csrf -> csrf.disable()) //csrf 보호 기능 off ,JWT 기반 인증에서는 필요없음
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/sign", "/login", "/").permitAll()
                        .anyRequest().authenticated()
                )//특정경로의 요청 설정 (회원가입, 로그인은 인증 없이 접근 허용, 그 외 모든 요청 인증 필요)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//JWT는 서버가 상태 저장하지 않으니 필수 설정
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) //jwtTokenFilter가 UsernamePasswordAuthenticationFilter 전에 동작하도록 설정
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
