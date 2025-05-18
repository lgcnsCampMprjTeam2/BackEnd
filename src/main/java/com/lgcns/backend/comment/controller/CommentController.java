package com.lgcns.backend.comment.controller;

import com.lgcns.backend.comment.service.CommentService;
import com.lgcns.backend.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lgcns.backend.comment.dto.CommentRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comm")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<CustomResponse<?>> getCommentList(@PathVariable Long postId) {
        return ResponseEntity.ok(CustomResponse.ok(commentService.getCommentList(postId)));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CustomResponse<?>> addComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request){

        //TODO 사용자 id
        Long userId = 1L;
        return ResponseEntity.ok(CustomResponse.ok(commentService.addComment(postId, userId, request)));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CustomResponse<?>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request){

        //TODO 사용자 id
        Long userId = 1L;

        return ResponseEntity.ok(CustomResponse.ok(commentService.updateComment(commentId, userId, request)));

    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CustomResponse<?>> deleteComment(
            @PathVariable Long commentId){

        //TODO 사용자 id
        Long userId = 1L;

        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(CustomResponse.ok(null));

    }

}
