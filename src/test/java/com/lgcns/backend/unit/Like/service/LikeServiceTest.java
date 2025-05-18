package com.lgcns.backend.unit.Like.service;

import com.lgcns.backend.Like.entity.Like;
import com.lgcns.backend.Like.repository.LikeRepository;
import com.lgcns.backend.Like.service.LikeService;
import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;


    private User user;
    private Comment comment;
    private Like like;

    @BeforeEach
    void setUp() {
        CSQuestion question = CSQuestion.builder()
                .id(1L)
                .content("질문")
                .category(Category.기타)
                .createdAt(LocalDateTime.now())
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .category(Category.기타)
                .user(user)
                .csQuestion(question)
                .createdAt(LocalDateTime.now())
                .build();

        comment = Comment.builder()
                .id(1L)
                .user(user)
                .post(post)
                .content("내용")
                .createdAt(LocalDateTime.now())
                .build();

        user = User.builder()
                .id(1L)
                .email("테스트@email.com")
                .password("1234")
                .name("임지빈")
                .nickname("데이나")
                .profileImage("https://example.com/profile.png")
                .provider("provider")
                .role("role")
                .build();

        like = Like.builder()
                .id(1L)
                .comment(comment)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("좋아요 버튼 설정, 해제")
    public void toggleLike() {
        //Case 1: 좋아요 없으면 -> 생성
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.empty());

        likeService.toggleLike(1L, 1L);

        verify(likeRepository).save(any(Like.class));

        // Case 2: 좋아요가 이미 있는 경우 → 삭제
        reset(likeRepository); // mock 상태 초기화
        when(likeRepository.findByCommentAndUser(comment, user)).thenReturn(Optional.of(like));

        likeService.toggleLike(1L, 1L);

        verify(likeRepository).delete(like);

    }

}