package com.lgcns.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lgcns.backend.global.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    @Getter
    @Builder
    public static class PostCreateResponse{
        private Long id;
        private Long userId;
        private Long questionId;
        private String title;
        private String content;
        private Category category;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class PostUpdateResponse{
        private Long id;

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("question_id")
        private Long questionId;
        private String title;
        private String content;
        private Category category;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class PostListResponse {
        private List<PostSummary> posts;

        @Getter
        @Builder
        public static class PostSummary {

            private Long id;
            private String title;
            private Category category;

            @JsonProperty("created_at")
            private LocalDateTime createdAt;
        }
    }

    @Getter
    @Builder
    public static class PostDetailResponse {

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("question_id")
        private Long questionId;

        private String content;
        private String title;
        private Category category;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

}