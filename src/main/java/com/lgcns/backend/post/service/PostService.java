package com.lgcns.backend.post.service;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.domain.Post;
import com.lgcns.backend.post.dto.PostRequest;
import com.lgcns.backend.post.dto.PostResponse;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.lgcns.backend.post.dto.PostRequest.*;
import static com.lgcns.backend.post.dto.PostResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CSQuestionRepository cSQuestionRepository;

    //게시글 목록 조회
    public PostListResponse getPostList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return PostListResponse.from(posts);
    }

    //카테고리별 목록 조회
    public PostListResponse getPostListByCategory(String category, Pageable pageable) {
        Category enumCategory = Category.valueOf(category.toUpperCase());
        Page<Post> posts = postRepository.findByCategory(enumCategory, pageable);
        return PostListResponse.from(posts);
    }

    //게시글 상세 조회
    public PostDetailResponse getPostDetail(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("not found"));
        return PostDetailResponse.from(post);
    }

    //게시글 생성
    @Transactional(readOnly = false)
    public PostCreateResponse createPost(PostCreateRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));
        CSQuestion csQuestion = cSQuestionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new EntityNotFoundException("질문을 찾을 수 없습니다"));

        Post post = Post.builder()
                .user(user)
                .csQuestion(csQuestion)
                .content(request.getContent())
                .title(request.getTitle())
                .category(Category.valueOf(request.getCategory()))
                .build();

        postRepository.save(post);
        return PostCreateResponse.from(post);
    }

    //게시글 수정
    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest request, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!Objects.equals(post.getUser().getId(), userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }

        post.updatePost(Category.valueOf(request.getCategory()),request.getTitle(), request.getContent());
        return PostUpdateResponse.from(post);
    }

    //게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
