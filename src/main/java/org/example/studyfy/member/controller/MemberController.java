package org.example.studyfy.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.model.MemberRequest;
import org.example.studyfy.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입
    @PostMapping("/sign")
    public Member create(
            @Valid
            @RequestBody MemberRequest request
    ){
        Member memberEntity = request.toEntity();
        return memberService.create(memberEntity);
    }


}
