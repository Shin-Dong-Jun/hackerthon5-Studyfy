package org.example.studyfy.member.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findFirstByEmail(String email);

    Optional<Member> findByEmail(String email);
}
