package com.lgcns.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

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

        public static PostCreateResponse from(Post post) {
            return PostCreateResponse.builder()
                    .id(post.getId())
                    .userId(post.getUser().getId())
                    .questionId(post.getCsQuestion().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
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

        public static PostUpdateResponse from(Post post) {
            return PostUpdateResponse.builder()
                    .id(post.getId())
                    .userId(post.getUser().getId())
                    .questionId(post.getCsQuestion().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
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

            public static PostSummary fromEntity(Post post) {
                return PostSummary.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .category(post.getCategory())
                        .createdAt(post.getCreatedAt())
                        .build();
            }
        }

        public static PostListResponse from(Page<Post> postPage) {
            List<PostSummary> postSummaries = postPage.getContent().stream()
                    .map(PostSummary::fromEntity)
                    .toList();

            return PostListResponse.builder()
                    .posts(postSummaries)
                    .build();
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

        public static PostDetailResponse from(Post post) {
            return PostDetailResponse.builder()
                    .userId(post.getUser().getId())
                    .questionId(post.getCsQuestion().getId())
                    .content(post.getContent())
                    .title(post.getTitle())
                    .category(post.getCategory())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }




}