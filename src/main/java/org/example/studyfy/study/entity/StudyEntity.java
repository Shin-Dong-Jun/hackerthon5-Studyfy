package org.example.studyfy.study.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.studyfy.BaseEntity;
import org.example.studyfy.study.dto.StudyRequestDto;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "study")
public class StudyEntity extends BaseEntity {

    @JoinColumn(name = "member_id", nullable = false)
    private Long creatorId; // 유저 ID 작성자

    @JoinColumn(name = "category_id", nullable = false)
    private Long categoryId; // 카테고리 ID

    @Column(nullable = false)
    private String title; // 스터디명

    @Column(nullable = false)
    private String goal; // 목표

    @Column(nullable = false)
    private String description; // 스터디 설명

    @Column(nullable = false)
    private Integer max_participants; // 최대 참가자 수

    @Column(nullable = false)
    private String method; // 스터디 진행 방식

    @Column(nullable = false)
    private LocalDateTime duration_start; // 시작일

    @Column(nullable = false)
    private LocalDateTime duration_end; // 종료일

    @Column(nullable = false)
    private LocalDateTime deadline; // 모집 마감일


    // 매장 정보 수정
    public void updateStudy(Long categoryId, String title, String goal, String description,
                             int max_participants, String method,
                             LocalDateTime duration_start, LocalDateTime duration_end, LocalDateTime deadline) {
        this.categoryId = categoryId;
        this.title = title;
        this.goal = goal;
        this.description = description;
        this.max_participants = max_participants;
        this.method = method;
        this.duration_start = duration_start;
        this.duration_end = duration_end;
        this.deadline = deadline;
    }
}
