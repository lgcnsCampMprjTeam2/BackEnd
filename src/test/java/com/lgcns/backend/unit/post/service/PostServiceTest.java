package com.lgcns.backend.unit.post.service;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.post.service.PostService;
import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lgcns.backend.post.dto.PostRequest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks //테스트 대상 객체 생성 + mock 주입
    private PostService postService;

    @Mock //가짜 객체 생성
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CSQuestionRepository csQuestionRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        User user = User.builder()
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
    }

    @Test
    @DisplayName("게시글 목록 불러오기")
    void getPostList(){
        // given
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Post> page = new PageImpl<>(List.of(post));

        when(postRepository.findAll(pageable)).thenReturn(page);

        // when
        var response = postService.getPostList(pageable);

        // then
        assertNotNull(response);
        assertEquals(1, response.getPosts().size());
        assertEquals("제목", response.getPosts().get(0).getTitle());

        verify(postRepository).findAll(pageable); // 메서드가 실제 호출되었는지 확인
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void getPostDetail(){
        //given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        var response = postService.getPostDetail(1L);

        //then
        assertNotNull(response);
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(post.getContent(), response.getContent());
        assertEquals(post.getCategory(), response.getCategory());
        assertEquals(post.getCreatedAt(), response.getCreatedAt());

        verify(postRepository).findById(1L);
    }


    @Test
    @DisplayName("게시글 생성")
    public void createPost() {
        //given
        var request = PostCreateRequest.builder()
                .title("제목")
                .content("내용")
                .questionId(1L)
                .category("기타")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(post.getUser()));
        when(csQuestionRepository.findById(1L)).thenReturn(Optional.of(post.getCsQuestion()));
        //인자로 어떤 Post 객체가 들어오든 상관없이 내가 미리 만든 post 객체를 리턴
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        var response = postService.createPost(request, 1L);

        //then
        assertNotNull(response);
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());
        assertEquals(1L, response.getQuestionId());
        assertEquals(Category.기타, response.getCategory());


        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost() {
        //given
        var request = PostUpdateRequest.builder()
                .title("제목")
                .content("내용")
                .category("기타")
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        var response = postService.updatePost(1L, request, 1L);

        //then
        assertNotNull(response);
        assertEquals("제목", response.getTitle());
        assertEquals("내용", response.getContent());
        assertEquals(1L, response.getQuestionId());
        assertEquals(Category.기타, response.getCategory());

        verify(postRepository).findById(1L);

    }

    @Test
    @DisplayName("게시글 삭제")
    public void deletePost() {
        //given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        postService.deletePost(1L, 1L);

        //then
        verify(postRepository).delete(post);

    }
}