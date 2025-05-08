package org.example.studyfy.study.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private Long creatorId;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "목표는 필수입니다.")
    private String goal;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @Min(value = 1, message = "최대 참가자는 최소 1명 이상이어야 합니다.")
    private int max_participants;

    @NotBlank(message = "진행 방식은 필수입니다.")
    private String method;

    @NotNull(message = "시작일은 필수입니다.")
    private LocalDateTime duration_start;

    @NotNull(message = "종료일은 필수입니다.")
    private LocalDateTime duration_end;

    @NotNull(message = "모집 마감일은 필수입니다.")
    private LocalDateTime deadline;


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
