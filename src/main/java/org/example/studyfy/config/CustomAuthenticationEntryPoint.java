package org.example.studyfy.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException; //인증 과정에서 발생하는 예외 처리
import org.springframework.security.web.AuthenticationEntryPoint; //Spring Security에서 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출하는 인터페이스
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //인증 실패 시, 원하는 응답 반환 처리 메소드
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus((HttpServletResponse.SC_UNAUTHORIZED)); //HTTP 상태 코드를 401(인증 실패)로 설정
        response.setContentType("application/json"); //응답의 Content-Type을 JSON으로 설정
        response.setCharacterEncoding("UTF-8"); //응답의 문자 인코딩을 UTF-8로 설정하여 한글 등이 깨지지 않게 설정
        response.getWriter().write("{\"error\": \"인증이 필요합니다. 토큰이 없거나 유효하지 않습니다.\"}"); //응답 본문
    }
}
