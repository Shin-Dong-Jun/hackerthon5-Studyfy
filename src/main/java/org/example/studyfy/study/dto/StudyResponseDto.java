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
public class StudyResponseDto {

    private Long id;
    private Long creatorId;
    private Long categoryId;
    private String title;
    private String goal;
    private String description;
    private int maxParticipants;
    private String method;
    private LocalDateTime durationStart;
    private LocalDateTime durationEnd;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static StudyResponseDto fromEntity(StudyEntity entity) {
        return StudyResponseDto.builder()
                .id(entity.getId())
                .creatorId(entity.getCreatorId())
                .categoryId(entity.getCategoryId())
                .title(entity.getTitle())
                .goal(entity.getGoal())
                .description(entity.getDescription())
                .maxParticipants(entity.getMax_participants())
                .method(entity.getMethod())
                .durationStart(entity.getDuration_start())
                .durationEnd(entity.getDuration_end())
                .deadline(entity.getDeadline())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
