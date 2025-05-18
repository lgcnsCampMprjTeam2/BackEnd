package com.lgcns.backend.unit.comment.service;

import com.lgcns.backend.comment.dto.CommentRequest;
import com.lgcns.backend.comment.dto.CommentResponse;
import com.lgcns.backend.comment.entity.Comment;
import com.lgcns.backend.comment.respository.CommentRepository;
import com.lgcns.backend.comment.service.CommentService;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
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
import java.util.List;
import java.util.Optional;

import static com.lgcns.backend.comment.dto.CommentRequest.*;
import static com.lgcns.backend.comment.dto.CommentResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp(){
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

        CSQuestion question = CSQuestion.builder()
                .id(1L)
                .content("질문")
                .category(Category.기타)
                .createdAt(LocalDateTime.now())
                .build();

        post = Post.builder()
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

    }

    @Test
    @DisplayName("댓글 목록 조회")
    public void getCommentList() {
        //given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(List.of(comment));

        //when
        CommentListResponse response = commentService.getCommentList(1L);

        //then
        assertNotNull(response);
        assertEquals(1, response.getComments().size());
        assertEquals("내용", response.getComments().get(0).getContent());

        verify(commentRepository).findByPost(post);
    }

    @Test
    @DisplayName("댓글 작성")
    public void createComment() {
        //given
        var request = CommentCreateRequest.builder()
                .content("내용")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        //when
        CommentCreateResponse response = commentService.addComment(1L, 1L, request);

        //then
        assertNotNull(response);
        assertEquals("내용", response.getContent());

        verify(commentRepository).save(any(Comment.class));

    }

    @Test
    @DisplayName("댓글 수정 ")
    public void updateComment() {
        //given
        var request = CommentUpdateRequest.builder()
                .content("수정 내용")
                .build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment)); // ✅ 추가

        //when
        CommentUpdateResponse response = commentService.updateComment(1L, 1L, request);

        //then
        assertNotNull(response);
        assertEquals("수정 내용", response.getContent());

    }

    @Test
    @DisplayName("댓글삭제")
    public void deleteComment() {
        //given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        //when
        commentService.deleteComment(1L, 1L);

        //then
        verify(commentRepository).delete(comment);

    }

}