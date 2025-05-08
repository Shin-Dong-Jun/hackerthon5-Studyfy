package org.example.studyfy.studyApplication.dto;

import org.example.studyfy.studyApplication.domain.StudyApplication;

public record ApplicationResponse(
        Long id,
        Long studyId,
        Long applicantId,
        String status
) {

    // Entity -> Dto 변환을 위한 정적 팩토리 메서드
    public static ApplicationResponse fromEntity(StudyApplication app) {
        return new ApplicationResponse(
                app.getId(),
                app.getStudy().getId(),
                app.getApplicatedMember().getId(),
                app.getApplicationStatus().name()
        );
    }
}
