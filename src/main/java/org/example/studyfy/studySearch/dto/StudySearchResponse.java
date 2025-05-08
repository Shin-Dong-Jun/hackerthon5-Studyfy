package org.example.studyfy.studySearch.dto;

import java.time.LocalDateTime;

public record StudySearchResponse(

        String title,
        LocalDateTime duration_start,
        LocalDateTime duration_end,
        LocalDateTime deadLine
) {
}
