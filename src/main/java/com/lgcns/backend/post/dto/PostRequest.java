package com.lgcns.backend.post.dto;

import lombok.Builder;
import lombok.Getter;

//게시글 작성 또는 수정 시, 요청 데이터를 받을 때 사용
public class PostRequest {

    @Getter
    @Builder
    public static class PostCreateRequest {
        private String title;
        private Long questionId;
        private String content;
        private String category;
    }




}