package org.example.studyfy.study.repository;

import org.example.studyfy.study.entity.StudyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<StudyEntity, Long> {


}
