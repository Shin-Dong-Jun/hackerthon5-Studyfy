package org.example.studyfy.contoroller;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.config.JwtTokenProvider;
import org.example.studyfy.member.db.MemberRepository;
import org.example.studyfy.member.model.LoginDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final MemberRepository memberRepository;  //이메일 찾기용
    private final PasswordEncoder passwordEncoder;  //암호화된 비밀번호 비교용
    private final JwtTokenProvider jwtTokenProvider; //로그인 성공 시 JWT 토큰을 생성하기 위해 사용

    @PostMapping("/login")
    //클라이언트가 보낸 JSON 데이터를 LoginDto 객체로 매핑.
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        //이메일로 member 조회 : 없으면 예외처리
        var user = memberRepository.findFirstByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));
        
        //패스워드 일치하는지 확인 : 틀리면 예외처리
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }

        //위 조건을 통과하면 이메일 정보를 기반으로 JWT 토큰 생성
        var token = jwtTokenProvider.createToken(user.getEmail());
        //토큰 반환
        return ResponseEntity.ok(Map.of("token", token));
    }
}
