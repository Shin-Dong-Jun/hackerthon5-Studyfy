package org.example.studyfy.studySearch.controller;

import lombok.RequiredArgsConstructor;
import org.example.studyfy.studySearch.dto.StudySearchResponse;
import org.example.studyfy.studySearch.service.StudySearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/studies")
@RestController
@RequiredArgsConstructor
public class StudySearchController {

    private final StudySearchService studySearchService;

    @GetMapping("/search")
    public ResponseEntity<List<StudySearchResponse>> searchStudies(@RequestParam String keyword){
        List<StudySearchResponse> result = studySearchService.searchByKeyword(keyword);

        return ResponseEntity.ok(result);

    }
}
