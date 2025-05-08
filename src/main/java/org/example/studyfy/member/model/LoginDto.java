package org.example.studyfy.member.model;

import lombok.Getter;
import lombok.Setter;

//로그인 요청 DTO
@Getter
@Setter
public class LoginDto {
    private String email;
    private String password;
}
