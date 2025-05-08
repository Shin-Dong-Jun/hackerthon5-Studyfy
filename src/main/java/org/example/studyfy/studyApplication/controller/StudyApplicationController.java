package org.example.studyfy.studyApplication.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.example.studyfy.studyApplication.domain.ApplicationStatus;
import org.example.studyfy.studyApplication.domain.StudyApplication;
import org.example.studyfy.studyApplication.dto.ApplicationResponse;
import org.example.studyfy.studyApplication.service.StudyApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class StudyApplicationController {

    private final StudyApplicationService studyApplicationService;
    private final MemberRepository memberRepository;

    // 신청하기
    @PostMapping("/study/{studyId}/apply")
    public ResponseEntity<ApplicationResponse> applyForStudy(@PathVariable Long studyId) {
        try {
            // 임시 사용자 ID (나중에 인증 붙이면 수정 예정)
            String userEmail = "test@example.com";
            StudyApplication application = studyApplicationService.applyToStudy(studyId, userEmail);
            return ResponseEntity.status(201).body(ApplicationResponse.fromEntity(application));
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("에러: >>>>>{}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // 신청 목록 조회
    @GetMapping("/study/{studyId}/pending")
    public ResponseEntity<List<ApplicationResponse>> getPendingApplications(@PathVariable Long studyId) {
        try {
            Member member = memberRepository.findByEmail("test@example.com").orElseThrow(() -> new IllegalStateException("Member not found"));
            List<StudyApplication> applications = studyApplicationService.getPendingApplicationsForStudyByCreator(studyId, member);
            List<ApplicationResponse> dtos = applications.stream()
                    .map(ApplicationResponse::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 신청 승인
    @PostMapping("/{applicationId}/approve")
    public ResponseEntity<ApplicationResponse> approveApplication(@PathVariable Long applicationId) {
        try {
            Member member = memberRepository.findByEmail("test@example.com").orElseThrow(() -> new IllegalStateException("Member not found"));
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.APPROVED, member);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 신청 거절
    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(@PathVariable Long applicationId) {
        try {
            Member member = memberRepository.findByEmail("test@example.com").orElseThrow(() -> new IllegalStateException("Member not found"));
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.REJECTED, member);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
