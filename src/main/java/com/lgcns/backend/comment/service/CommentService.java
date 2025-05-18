package com.lgcns.backend.comment.service;

import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.lgcns.backend.comment.dto.CommentRequest.*;
import static com.lgcns.backend.comment.dto.CommentResponse.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 목록 조회
    public CommentListResponse getCommentList(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByPost(post);
        return CommentListResponse.from(comments);
    }

    //댓글 작성
    @Transactional
    public CommentCreateResponse addComment(Long postId, Long userId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return CommentCreateResponse.from(comment);
    }

    //댓글 수정
    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));

        if (!Objects.equals(comment.getUser().getId(), userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }

        comment.updateComment(request.getContent());
        return CommentUpdateResponse.from(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));

        if (!Objects.equals(comment.getUser().getId(), userId)){
            throw new IllegalArgumentException("삭제 권한이 없습니다");
        }

        commentRepository.delete(comment);
    }

}
