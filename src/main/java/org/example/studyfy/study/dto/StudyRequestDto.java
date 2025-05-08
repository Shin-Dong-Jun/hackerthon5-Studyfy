package org.example.studyfy.study.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.studyfy.study.entity.StudyEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRequestDto {
    private Long creatorId; // member 추후 삭제
    private Long categoryId;
    private String title;
    private String goal;
    private String description;
    private int max_participants;
    private String method; // 스터디 진행 방식
    private LocalDateTime duration_start; // 시작일
    private LocalDateTime duration_end; // 종료일
    private LocalDateTime deadline; // 모집 마감일


    public StudyEntity toEntity(Long memberId) {
        return StudyEntity.builder()
                .creatorId(memberId)
                .categoryId(categoryId)
                .title(title)
                .goal(goal)
                .description(description)
                .max_participants(max_participants)
                .method(method)
                .duration_start(duration_start)
                .duration_end(duration_end)
                .deadline(deadline)
                .build();
    }
}
