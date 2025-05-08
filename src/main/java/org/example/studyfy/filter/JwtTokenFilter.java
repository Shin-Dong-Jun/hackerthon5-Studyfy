package org.example.studyfy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.studyfy.config.JwtTokenProvider;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//JWT 인증 필터
@Slf4j
@Component  //Spring Bean으로 등록
@RequiredArgsConstructor
//OncePerRequestFilter : 요청당 한 번만 실행되는 필터, 스프링 시큐리티의 기본 필터 구현체
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;


    //Http 요청마다 실행

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //클라이언트 요청의 Authorization 헤더에서 JWT 토큰 추출
        String header = request.getHeader("Authorization");
        //Bearer {token} 형식인지 확인
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7); //{token} 부분 추출

            //토큰 유효성 검증
            if(jwtTokenProvider.validateToken(token)) {
                //토큰에서 이메일 정보 추출
                String email = jwtTokenProvider.getEmail(token);
                Member user = memberRepository.findFirstByEmail(email).orElse(null); //값이 없을 경우, null 반환 /있으면 해당 값 반환


                //사용자가 존재할 경우
                if (user != null) {
                    //인증 객체 생성
                    //user는 principal로 설정되고, 비밀번호나 권한은 지금 생략되어 있음 -> 권한 추후 필요
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, List.of()
                    );
                    //위 인증 정보를 SecurityContex에 등록 -> 이후 처리 시 '로그인됨 사용자'로 인식 됨
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        log.info("Request URI: {}", request.getRequestURI());

        //인증 처리 후 다음 필터나 컨트롤러로 요청을 전달
        filterChain.doFilter(request, response);
    }
}
