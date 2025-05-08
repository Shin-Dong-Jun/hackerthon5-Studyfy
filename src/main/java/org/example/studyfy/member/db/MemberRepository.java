package org.example.studyfy.member.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {

    Optional<MemberEntity> findFirstByEmail(String email);

    Optional<MemberEntity> findByEmail(String email);
}
