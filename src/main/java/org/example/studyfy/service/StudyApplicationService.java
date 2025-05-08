package org.example.studyfy.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.domain.ApplicationStatus;
import org.example.studyfy.domain.Study;
import org.example.studyfy.domain.StudyApplication;
import org.example.studyfy.repository.StudyApplicationRepository;
import org.example.studyfy.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyApplicationService {

    private final StudyApplicationRepository studyApplicationRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public StudyApplication applyToStudy(Long studyId, String dummyUsername) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디를 찾을 수 없습니다: ID " + studyId));

        // 아직 Member 관련 구현이 안됐으므로, 임시로 null 또는 더미로 처리
        // 실제 구현 시 Member 객체를 DB에서 조회하여 사용해야 함
        // 예: Member applicant = memberRepository.findByUsername(dummyUsername).orElseThrow(...);

        // 여기서는 일단 멤버 없이 생성하도록 처리
        throw new UnsupportedOperationException("Member 엔티티 구현 이후 applyToStudy 로직을 완성해야 합니다.");
    }

    public List<StudyApplication> getPendingApplicationsForStudyByCreator(Long studyId, String dummyUsername) {
        Study study = studyRepository.findById(studyId)
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
