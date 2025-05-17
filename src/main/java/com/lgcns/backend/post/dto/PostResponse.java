package com.lgcns.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostResponse {

    @Getter
    @Builder
    public static class PostCreateResponse{
        private Long id;
        private Long userId;
        private Long questionId;
        private String title;
        private String content;
        private String category;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }


}