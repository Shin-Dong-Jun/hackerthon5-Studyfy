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
    public StudyApplication applyToStudy(Long studyId, String email) {
        StudyEntity study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다: ID " + studyId));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        StudyApplication application = new StudyApplication(study, member);
        return studyApplicationRepository.save(application);
    }

    public List<StudyApplication> getPendingApplicationsForStudyByCreator(Long studyId, Member member) {
        StudyEntity study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다: ID " + studyId));

        // 개설자 검증 로직은 Member 구현 후 추가
        if(!study.getCreatorId().equals(member.getId())) {
            throw new SecurityException("해당 스터디에 대한 권한이 없습니다.");
        }
        return studyApplicationRepository.findByStudyAndApplicationStatus(study, ApplicationStatus.PENDING);
    }

    @Transactional
    public StudyApplication processApplication(Long applicationId, ApplicationStatus newStatus, Member member) {
        StudyApplication application = studyApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("신청 정보를 찾을 수 없습니다: ID " + applicationId));

        StudyEntity study= application.getStudy();

        // 개설자 검증 로직은 Member 구현 후 추가
        if(!study.getCreatorId().equals(member.getId())) {
            throw new SecurityException("신청 처리 권한이 없습니다.");
        }

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
