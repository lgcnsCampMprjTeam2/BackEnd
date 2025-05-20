package com.lgcns.backend.post.controller;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.response.CustomResponse;
import com.lgcns.backend.post.dto.PostRequest;
import com.lgcns.backend.post.service.PostService;
import com.lgcns.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.lgcns.backend.post.dto.PostRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comm")
public class PostController {

    private final PostService postService;
    private final CSQuestionRepository cSQuestionRepository;

    @GetMapping
    public ResponseEntity<CustomResponse<?>> getPostList(
            @RequestParam(required = false) String category,
            Pageable pageable
    ){
        if (category != null) {
            return ResponseEntity.ok(CustomResponse.ok(postService.getPostListByCategory(category, pageable)));
        } else {
            return ResponseEntity.ok(CustomResponse.ok(postService.getPostList(pageable)));
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CustomResponse<?>> getPostDetail(
            @PathVariable Long postId
    ){
        return ResponseEntity.ok(CustomResponse.ok(postService.getPostDetail(postId)));
    }

    @PostMapping
    public ResponseEntity<CustomResponse<?>> createPost(
            @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal User user
    ){
        CSQuestion csQuestion = Optional.ofNullable(request.getQuestionId())
                .flatMap(cSQuestionRepository::findById)
                .orElse(null);

        Long userId = user.getId();
        return ResponseEntity.ok(CustomResponse.ok(postService.createPost(request, userId)));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CustomResponse<?>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal User user
    ){
        Long userId = user.getId();
        return ResponseEntity.ok(CustomResponse.ok(postService.updatePost(postId, request, userId)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CustomResponse<?>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ){
        Long userId = user.getId();
        postService.deletePost(postId, userId);
        return ResponseEntity.ok(CustomResponse.ok(null));
    }


}
