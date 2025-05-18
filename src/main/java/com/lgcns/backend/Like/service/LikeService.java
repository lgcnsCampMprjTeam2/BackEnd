package com.lgcns.backend.Like.service;

import com.lgcns.backend.Like.dto.LikeResponse;
import com.lgcns.backend.Like.entity.Like;
import com.lgcns.backend.Like.repository.LikeRepository;
import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public LikeResponse toggleLike(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        Optional<Like> existingLike = likeRepository.findByCommentAndUser(comment, user);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return LikeResponse.from(comment.getId(), false);
        } else {
            Like like = Like.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            likeRepository.save(like);

            return LikeResponse.from(comment.getId(), true);
        }

    }

}
