package org.example.studyfy.member.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member create(
            Member memberEntity
    ){
        var optionalMember = memberRepository.findFirstByEmail(memberEntity.getEmail());

        //이미 존재하는 회원인지 확인 여부
        if(optionalMember.isPresent()){
            var existingMember = optionalMember.get();
            //있으면 기존 회원 정보 반환
            return existingMember;
        }else{
            var entity = Member.builder()
                    .userName(memberEntity.getUserName())
                    .password(memberEntity.getPassword())
                    .email(memberEntity.getEmail())
                    .gender(memberEntity.getGender())
                    .build();

            //없으면 저장
            return memberRepository.save(entity);
        }
    }

}
