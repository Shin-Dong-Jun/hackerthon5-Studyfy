package org.example.studyfy.member.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value= PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberDto {

    private String userName;

    private String password;

    private String email;

    private String gender;
}
