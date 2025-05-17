package com.lgcns.backend.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lgcns.backend.global.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {

    @Getter
    @Builder
    public static class CommentCreateResponse{
        private Long id;

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("post_id")
        private Long postId;

        private String content;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class CommentUpdateResponse{
        private Long id;

        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("post_id")
        private Long postId;

        private String content;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class CommentListResponse {
        private List<CommentSummary> comments;

        @Getter
        @Builder
        public static class CommentSummary {

            @JsonProperty("comment_id")
            private Long commentId;

            @JsonProperty("user_id")
            private Long userId;
            private String content;

            @JsonProperty("created_at")
            private LocalDateTime createdAt;
        }
    }
}
