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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    //SecurityFilterChain : Spring Security의 핵심 보안 체인
    //HttpSecurity : Http 요청에 대해 보안 설정을 정의 하는 객체
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable()) //csrf 보호 기능 off ,JWT 기반 인증에서는 필요없음
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//JWT는 서버가 상태 저장하지 않으니 필수 설정
                .formLogin(form -> form.disable()) //Spring Security의 기본 로그인 폼 기능을 비활성화
                .httpBasic(basic -> basic.disable()) //Spring Security의 HTTP Basic 인증 방식을 비활성
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/v1/member/sign", "api/v1/login", "/").permitAll()
                        .anyRequest().authenticated()
                )//특정경로의 요청 설정 (회원가입, 로그인은 인증 없이 접근 허용, 그 외 모든 요청 인증 필요)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint) //인증 실패 시 예외처리
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) //jwtTokenFilter가 UsernamePasswordAuthenticationFilter 전에 동작하도록 설정
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
