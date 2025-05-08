package org.example.studyfy.studySearch.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.study.entity.StudyEntity;
import org.example.studyfy.study.repository.StudyRepository;
import org.example.studyfy.studySearch.dto.StudySearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySearchService {

    private final StudyRepository studyRepository;

    public List<StudySearchResponse> searchByKeyword(String keyword) {

        List<StudyEntity> result = studyRepository.findByTitleContainingOrGoalContainingOrDescriptionContaining(keyword, keyword, keyword);

        return result.stream()
                .map(study -> new StudySearchResponse(
                        study.getTitle(),
                        study.getDuration_start(),
                        study.getDuration_end(),
                        study.getDeadline()
                ))
                .toList();
    }
}
