package org.example.studyfy.member.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.studyfy.member.db.Member;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberRequest  {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[MF]$", message = "성별은 'M' 또는 'F'여야 합니다.")
    @Builder.Default
    private String gender = "M";

    // DTO → Entity 변환 메서드
    public Member toEntity() {
        return Member.builder()
                .userName(this.userName)
                .password(this.password)
                .email(this.email)
                .gender(this.gender)
                .build();
    }

}
