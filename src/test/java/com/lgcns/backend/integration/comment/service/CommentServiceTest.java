package com.lgcns.backend.integration.comment.service;

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
@DisplayName("댓글 통합 테스트")
public class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CSQuestionRepository csQuestionRepository;

    private User user;
    private Post post;


    @BeforeEach
    void setUp() {
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

        post = postRepository.save(Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());
    }

    @Test
    @DisplayName("댓글 목록 조회")
    public void getCommentList() {
        //given
        commentRepository.save(Comment.builder()
                .content("댓글 내용1")
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build());

        commentRepository.save(Comment.builder()
                .content("댓글 내용2")
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build());

        //when
        List<Comment> all = commentRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("댓글 작성")
    public void createComment() {
        //given
        Comment comment = Comment.builder()
                .content("댓글 내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build();

        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        assertThat(savedComment.getId()).isEqualTo(comment.getId());
        assertThat(savedComment.getContent()).isEqualTo(comment.getContent());
        assertThat(savedComment.getCreatedAt()).isEqualTo(comment.getCreatedAt());
        assertThat(savedComment.getUser()).isEqualTo(comment.getUser());
        assertThat(savedComment.getPost()).isEqualTo(comment.getPost());

    }

    @Test
    @DisplayName("댓글 수정")
    public void updateComment() {
        //given
        Comment comment = Comment.builder()
                .content("댓글 내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build();

        //when
        comment.updateComment("수정 내용");
        Comment updatedComment = commentRepository.save(comment);

        //then
        assertThat(updatedComment.getId()).isEqualTo(comment.getId());
        assertThat(updatedComment.getContent()).isEqualTo("수정 내용");
        assertThat(updatedComment.getCreatedAt()).isEqualTo(comment.getCreatedAt());
        assertThat(updatedComment.getUser()).isEqualTo(comment.getUser());
        assertThat(updatedComment.getPost()).isEqualTo(comment.getPost());

    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteComment() {
        //given
        Comment comment = commentRepository.save(Comment.builder()
                .content("댓글 내용")
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build());

        //when
        commentRepository.delete(comment);

        //then
        boolean exists = commentRepository.existsById(comment.getId());
        assertThat(exists).isFalse();

    }

}
