package org.example.studyfy.study.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.study.dto.StudyRequestDto;
import org.example.studyfy.study.dto.StudyResponseDto;
import org.example.studyfy.study.entity.StudyEntity;
import org.example.studyfy.study.repository.StudyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    // 스터디 생성
    @Transactional
    public StudyResponseDto createStudy(Long memberId, StudyRequestDto requestDto) {
        // member
        /*User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));*/
        // category

        // study
        StudyEntity study = requestDto.toEntity(memberId, requestDto);
        StudyEntity savedStudy = studyRepository.save(study);

        return StudyResponseDto.fromEntity(savedStudy);
    }

    // 스터디 상세 조회
    public StudyResponseDto getStudy(Long id) {
        StudyEntity study = studyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시물 id를 찾을 수 없습니다."));
        return StudyResponseDto.fromEntity(study);
    }

    // 스터디 전체 조회
    @Transactional(readOnly = true)
    public List<StudyResponseDto> getAllStudies() {
        return studyRepository.findAll().stream()
                .map(StudyResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 스터디 수정
    @Transactional
    public StudyResponseDto updateStudy(Long id, StudyRequestDto request) {
        StudyEntity study = studyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Study not found with id: " + id));

        // Update fields
        study = StudyEntity.builder()
//                .creatorId(request.get)
                .categoryId(request.getCategoryId())
                .title(request.getTitle())
                .goal(request.getGoal())
                .description(request.getDescription())
                .max_participants(request.getMax_participants())
                .method(request.getMethod())
                .duration_start(request.getDuration_start())
                .duration_end(request.getDuration_end())
                .deadline(request.getDeadline())
                .build();

        // BaseEntity의 ID를 직접 설정할 방법이 없어 이 부분은 실제 구현 시 수정 필요

        StudyEntity updatedEntity = studyRepository.save(study);
        return StudyResponseDto.fromEntity(updatedEntity);
    }

    // 스터디 삭제
    @Transactional
    public void deleteStudy(Long id) {
        studyRepository.deleteById(id);
    }
}
