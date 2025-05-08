package org.example.studyfy.study.repository;

import org.example.studyfy.study.entity.StudyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<StudyEntity, Long> {

    List<StudyEntity> findByTitleContainingOrGoalContainingOrDescriptionContaining(String title, String goal, String description);



}
