package com.lgcns.backend.csquestion.dto;

import java.time.LocalDateTime;

import com.lgcns.backend.global.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CSQuestionResponse {

    private Long id;
    private Category category;
    private LocalDateTime createdAt;
    private String content;
}
