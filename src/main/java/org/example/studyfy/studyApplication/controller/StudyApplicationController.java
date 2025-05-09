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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applications")
public class StudyApplicationController {

    private final StudyApplicationService studyApplicationService;
    private final MemberRepository memberRepository;

    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Member) authentication.getPrincipal();
    }

    // 신청하기
    @PostMapping("/study/{studyId}/apply")
    public ResponseEntity<ApplicationResponse> applyForStudy(@PathVariable Long studyId) {
        try {
            Member member = getCurrentMember();
            StudyApplication application = studyApplicationService.applyToStudy(studyId, member.getEmail());
            return ResponseEntity.status(201).body(ApplicationResponse.fromEntity(application));
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("에러: >>>>>{}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // 신청 목록 조회
    @GetMapping("/study/{studyId}/pending")
    public ResponseEntity<List<ApplicationResponse>> getPendingApplications(@PathVariable Long studyId, @AuthenticationPrincipal Member member) {
        try {
            Member memberEntity = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new IllegalStateException("Member not found"));
            List<StudyApplication> applications = studyApplicationService.getPendingApplicationsForStudyByCreator(studyId, memberEntity);
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
    public ResponseEntity<ApplicationResponse> approveApplication(@PathVariable Long applicationId, @AuthenticationPrincipal Member member) {
        try {
            Member memberEntity = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new IllegalStateException("Member not found"));
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.APPROVED, memberEntity);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 신청 거절
    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(@PathVariable Long applicationId, @AuthenticationPrincipal Member member) {
        try {
            Member memberEntity = memberRepository.findByEmail(member.getEmail()).orElseThrow(() -> new IllegalStateException("Member not found"));
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.REJECTED, memberEntity);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
