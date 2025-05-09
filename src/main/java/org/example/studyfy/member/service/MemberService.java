package org.example.studyfy.member.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.exception.InvalidLoginException;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.example.studyfy.member.model.MemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; //패스워드 암호화용

    public Member create(
            Member memberEntity
    ){
        var optionalMember = memberRepository.findFirstByEmail(memberEntity.getEmail());
        //패스워드 암호화
        String encodedPassword = passwordEncoder.encode(memberEntity.getPassword());

        //이미 존재하는 회원인지 확인 여부
        if (optionalMember.isPresent()) {
            //있으면 예외처리
            throw  new InvalidLoginException("이미 존재하는 이메일입니다.");  
        }else{
            var entity = Member.builder()
                    .userName(memberEntity.getUserName())
                    .password(encodedPassword)
                    .email(memberEntity.getEmail())
                    .gender(memberEntity.getGender())
                    .build();

            //없으면 저장
            return memberRepository.save(entity);
        }
    }
}
