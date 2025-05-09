package org.example.studyfy.member.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.example.studyfy.BaseEntity;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="member")
public class Member extends BaseEntity {

    @Comment("user 이름")
    private String userName;

    @Comment("비밀번호")
    private String password;

    @Comment("이메일")
    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "CHAR(1) CHARACTER SET utf8mb4")
    @Comment("성별")
    private String gender;

}
