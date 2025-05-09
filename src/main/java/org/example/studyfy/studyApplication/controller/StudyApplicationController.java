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

    /**
     * SecurityContext에서 현재 인증된 사용자 정보를 가져오는 메소드
     * @return 현재 인증된 Member 객체
     * @throws SecurityException 인증된 사용자가 없거나 유효하지 않은 경우
     */
    private Member getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("인증된 사용자가 아닙니다.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Member)) {
            throw new SecurityException("유효한 사용자 정보가 아닙니다.");
        }

        return (Member) principal;
    }

    /**
     * 스터디 신청하기
     * - 인증된 사용자만 신청 가능
     */
    @PostMapping("/study/{studyId}/apply")
    public ResponseEntity<ApplicationResponse> applyForStudy(@PathVariable Long studyId) {
        try {
            // SecurityContext에서 현재 인증된 사용자 정보 가져오기
            Member currentUser = getCurrentMember();

            // 스터디 신청 처리
            StudyApplication application = studyApplicationService.applyToStudy(studyId, currentUser.getEmail());
            return ResponseEntity.status(201).body(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            log.error("인증 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("신청 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 신청 목록 조회
     * - 스터디 생성자만 조회 가능
     */
    @GetMapping("/study/{studyId}/pending")
    public ResponseEntity<List<ApplicationResponse>> getPendingApplications(@PathVariable Long studyId) {
        try {
            // SecurityContext에서 현재 인증된 사용자 정보 가져오기
            Member currentUser = getCurrentMember();

            // 스터디 신청 목록 조회
            List<StudyApplication> applications = studyApplicationService.getPendingApplicationsForStudyByCreator(studyId, currentUser);

            List<ApplicationResponse> dtos = applications.stream()
                    .map(ApplicationResponse::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (SecurityException e) {
            log.error("인증 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(403).body(null);
        } catch (IllegalArgumentException e) {
            log.error("조회 오류: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 신청 승인
     * - 스터디 생성자만 승인 가능
     */
    @PostMapping("/{applicationId}/approve")
    public ResponseEntity<ApplicationResponse> approveApplication(@PathVariable Long applicationId) {
        try {
            // SecurityContext에서 현재 인증된 사용자 정보 가져오기
            Member currentUser = getCurrentMember();

            // 신청 승인 처리
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.APPROVED, currentUser);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            log.error("인증 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("승인 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 신청 거절
     * - 스터디 생성자만 거절 가능
     */
    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(@PathVariable Long applicationId) {
        try {
            // SecurityContext에서 현재 인증된 사용자 정보 가져오기
            Member currentUser = getCurrentMember();

            // 신청 거절 처리
            StudyApplication application = studyApplicationService.processApplication(applicationId, ApplicationStatus.REJECTED, currentUser);
            return ResponseEntity.ok(ApplicationResponse.fromEntity(application));
        } catch (SecurityException e) {
            log.error("인증 오류: {}", e.getMessage(), e);
            return ResponseEntity.status(403).body(null);
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("거절 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
