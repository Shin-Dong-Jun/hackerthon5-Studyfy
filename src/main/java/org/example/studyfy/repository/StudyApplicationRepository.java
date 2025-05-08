package org.example.studyfy.repository;


import org.example.studyfy.domain.ApplicationStatus;
import org.example.studyfy.domain.Study;
import org.example.studyfy.domain.StudyApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyApplicationRepository extends JpaRepository<StudyApplication, Long> {
    // 특정 스터디에서 PENDING 상태인 신청서 조회
    List<StudyApplication> findByStudyAndApplicationStatus(Study study, ApplicationStatus applicationStatus);

    // Member 구현 이후에 추가할 수 있는 메서드 예시:
    // Optional<StudyApplication> findByApplicatedMemberAndStudy(Member member, Study study);
}
