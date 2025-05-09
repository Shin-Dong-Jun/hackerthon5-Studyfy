package org.example.studyfy.study.service;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.member.db.Member;
import org.example.studyfy.member.db.MemberRepository;
import org.example.studyfy.study.dto.StudyRequestDto;
import org.example.studyfy.study.dto.StudyResponseDto;
import org.example.studyfy.study.entity.StudyEntity;
import org.example.studyfy.study.repository.StudyRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    // 스터디 생성
    @Transactional
    public StudyResponseDto createStudy(StudyRequestDto requestDto) {
        // member
        Member currentUser = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long creatorId = currentUser.getId();

        memberRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // study
        StudyEntity study = requestDto.toEntity(creatorId);
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
                .orElseThrow(() -> new RuntimeException("해당 게시물 id를 찾을 수 없습니다.: " + id));

        Member currentUser = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateCreatorId(study, currentUser.getId()); // 작성자 id 검증

        study.updateStudy(
                request.getCategoryId(),
                request.getTitle(),
                request.getGoal(),
                request.getDescription(),
                request.getMax_participants(),
                request.getMethod(),
                request.getDuration_start(),
                request.getDuration_end(),
                request.getDeadline()
        );

        StudyEntity updatedEntity = studyRepository.save(study);
        return StudyResponseDto.fromEntity(updatedEntity);
    }

    // 스터디 삭제
    @Transactional
    public void deleteStudy(Long id) {
        StudyEntity study = studyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 게시물 id를 찾을 수 없습니다.: " + id));

        Member currentUser = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateCreatorId(study, currentUser.getId()); // 작성자 id 검증

        studyRepository.deleteById(id);
    }

    // 스터디 개설자 멤버 id 검증
    private void validateCreatorId(StudyEntity study, Long memberId) {
        if (!study.getCreatorId().equals(memberId)) {
            throw new RuntimeException("스터디 수정 권한이 없습니다.");
        }
    }
}
