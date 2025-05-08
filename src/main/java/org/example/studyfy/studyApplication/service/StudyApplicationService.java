package org.example.studyfy.studyApplication.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.example.studyfy.study.entity.StudyEntity;
import org.example.studyfy.study.repository.StudyRepository;
import org.example.studyfy.studyApplication.domain.ApplicationStatus;
import org.example.studyfy.studyApplication.domain.StudyApplication;
import org.example.studyfy.studyApplication.repository.StudyApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyApplicationService {

    private final StudyApplicationRepository studyApplicationRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public StudyApplication applyToStudy(Long studyId, String dummyUsername) {
        StudyEntity study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다: ID " + studyId));

        // 더미 멤버 생성
        Member dummyMember = Member.builder()
                .userName("테스트유저")
                .password("temp")
                .email("temp@example.com")
                .gender("M")
                .build();

// 먼저 memberRepository에 저장
        Member savedDummyMember = memberRepository.save(dummyMember);

// 그 다음 StudyApplication 생성 및 저장
        StudyApplication application = new StudyApplication(study, savedDummyMember);
        return studyApplicationRepository.save(application);
    }

    public List<StudyApplication> getPendingApplicationsForStudyByCreator(Long studyId, String dummyUsername) {
        StudyEntity study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다: ID " + studyId));

        // 개설자 검증 로직은 Member 구현 후 추가
        return studyApplicationRepository.findByStudyAndApplicationStatus(study, ApplicationStatus.PENDING);
    }

    @Transactional
    public StudyApplication processApplication(Long applicationId, ApplicationStatus newStatus, String dummyUsername) {
        StudyApplication application = studyApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 정보를 찾을 수 없습니다: ID " + applicationId));

        // 개설자 검증 로직은 Member 구현 후 추가

        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 신청입니다.");
        }

        application.changeStatusTo(newStatus);
        // 처리 시각 관련 로직은 필요 시 BaseEntity 등에 추가 구현
        // 예: application.setProcessedAt(LocalDateTime.now());

        // 승인 시 스터디 멤버 추가하는 로직은 Member 구현 이후에
        return studyApplicationRepository.save(application);
    }
}
