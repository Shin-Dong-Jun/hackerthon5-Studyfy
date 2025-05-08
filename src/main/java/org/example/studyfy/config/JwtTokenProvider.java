package org.example.studyfy.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

//토큰 생성 및 검증
@Slf4j
@Component
public class JwtTokenProvider {

    //2바이트 크기의 랜덤 데이터를 생성하고 이를 Base64로 인코딩
    //@Value("${jwt.secret-key}")
    private final String secretKey = "jt0xTkKa4bVg8h2LmAun0o73iHwirNeEFMOeXvVmo1Y="; //변수로 빼야 함
    //토큰 유효 시간 설정 - 3시간
    private final long expiration = 1000L*60*60*3;

//    public JwtTokenProvider(){
//        Dotenv dotenv = Dotenv.load();
//        this.secretKey = dotenv.get("JWT_SECRET_KEy");
//    }

    //토큰 생성
    public String createToken(String email){
        log.info("Request URI: {} , 토큰 생성 시작",email);
        //Jwts(jjwt의 핵심 유틸리티 클래스)
        return Jwts.builder()
                .setSubject(email) //JWT 토큰의 subject
                .setIssuedAt(new Date()) //발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) //서명
                .compact();
    }

    //토큰에서 이메일 정보 추출
    public String getEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) //서명 검증에 사용할 비밀키
                .build()
                .parseClaimsJws(token) //JWT파싱
                .getBody()
                .getSubject(); //Subject(이메일) 반환
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        try {
            getEmail(token); //파싱 오류가 없으면 유효
            return true;
        }catch(Exception e){
            return false; //예외가 발생하면 토큰이 유효하지 않음
        }
    }
}
