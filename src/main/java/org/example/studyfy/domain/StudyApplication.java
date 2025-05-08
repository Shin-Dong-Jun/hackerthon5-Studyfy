package org.example.studyfy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.studyfy.BaseEntity;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "studyApplication")
public class StudyApplication extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member applicatedMember;

    @Column
    private ApplicationStatus applicationStatus;

    public StudyApplication(Study study, Member applicatedMember){
        this.study = study;
        this.applicatedMember = applicatedMember;
        this.applicationStatus = ApplicationStatus.PENDING;
    }

    public void changeStatusTo(ApplicationStatus newStatus) {
        this.applicationStatus = newStatus;
    }
}
