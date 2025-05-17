package com.lgcns.backend.comment.service;

import com.lgcns.backend.comment.dto.CommentRequest;
import com.lgcns.backend.comment.dto.CommentResponse;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    }

    //댓글 작성
    public CommentCreateResponse addComment(Long postId, Long userId, CommentCreateRequest request) {

    }

    //댓글 수정
    public CommentUpdateResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {

    }

    //댓글 삭제
    public void deleteComment(Long commentId, Long userId) {

    }

}
