package com.lgcns.backend.integration.like.service;

import com.lgcns.backend.Like.entity.Like;
import com.lgcns.backend.Like.repository.LikeRepository;
import com.lgcns.backend.Like.service.LikeService;
import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("좋아요 통합 테스트")
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CSQuestionRepository csQuestionRepository;

    private User user;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(User.builder()
                .email("email@email.com")
                .name("임지빈")
                .password("1234")
                .nickname("데이나")
                .profileImage("pofileImage.url")
                .provider("provider")
                .role("role")
                .build());

        CSQuestion question = csQuestionRepository.save(CSQuestion.builder()
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .build());

        Post post = postRepository.save(Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());

        comment = commentRepository.save(Comment.builder()
                .user(user)
                .post(post)
                .content("댓글 내용")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("좋아요 설정/해제")
    public void toggleLike() {
        //given
        likeService.toggleLike(comment.getId(), user.getId());

        //when
        List<Like> likesAfterSet = likeRepository.findAll();
        assertThat(likesAfterSet).hasSize(1);

        // then
        likeService.toggleLike(comment.getId(), user.getId());

        List<Like> likesAfterUnset = likeRepository.findAll();
        assertThat(likesAfterUnset).isEmpty();

    }
}
