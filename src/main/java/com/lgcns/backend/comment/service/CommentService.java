package com.lgcns.backend.comment.service;

import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.global.code.CommentErrorCode;
import com.lgcns.backend.global.exception.CustomException;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
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

    protected void validateWriter(Long writerId, Long currentUserId, com.lgcns.backend.global.code.CommentErrorCode errorCode) {
        if (!Objects.equals(writerId, currentUserId)) {
            throw new CustomException(errorCode);
        }
    }

    //댓글 목록 조회
    public CommentListResponse getCommentList(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(com.lgcns.backend.global.code.CommentErrorCode.POST_NOT_FOUND));
        List<Comment> comments = commentRepository.findByPost(post);
        return CommentListResponse.from(comments);
    }

    //댓글 작성
    @Transactional
    public CommentCreateResponse addComment(Long postId, Long userId, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.USER_NOT_FOUND));

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
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        validateWriter(comment.getUser().getId(), userId, CommentErrorCode.NO_UPDATE_PERMISSION);

        comment.updateComment(request.getContent());
        return CommentUpdateResponse.from(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        validateWriter(comment.getUser().getId(), userId, CommentErrorCode.NO_DELETE_PERMISSION);

        commentRepository.delete(comment);
    }

}
