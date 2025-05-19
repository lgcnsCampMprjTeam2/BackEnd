package com.lgcns.backend.integration.post.service;

import com.lgcns.backend.csquestion.domain.CSQuestion;
import com.lgcns.backend.csquestion.repository.CSQuestionRepository;
import com.lgcns.backend.global.domain.Category;
import com.lgcns.backend.post.dto.PostRequest;
import com.lgcns.backend.post.dto.PostResponse;
import com.lgcns.backend.post.entity.Post;
import com.lgcns.backend.post.respository.PostRepository;
import com.lgcns.backend.post.service.PostService;
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
import java.util.Optional;

import static com.lgcns.backend.post.dto.PostRequest.*;
import static com.lgcns.backend.post.dto.PostResponse.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("게시글 통합 테스트")
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CSQuestionRepository csQuestionRepository;

    private User user;
    private CSQuestion question;


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

        question = csQuestionRepository.save(CSQuestion.builder()
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .build());

    }


    @Test
    @DisplayName("게시글 목록 조회")
    public void getPostList(){
        // given
        postService.createPost(PostCreateRequest.builder()
                .title("제목1")
                .questionId(question.getId())
                .content("내용1")
                .category("네트워크")
                .build(), user.getId());

        postService.createPost(PostCreateRequest.builder()
                .title("제목2")
                .questionId(question.getId())
                .content("내용2")
                .category("알고리즘")
                .build(), user.getId());


        // when
        List<Post> all = postRepository.findAll();

        // then
        assertThat(all).hasSize(2);

    }

    @Test
    @DisplayName("게시글 상세 조회")
    public void getPostDetail() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("상세 조회 테스트")
                .questionId(question.getId())
                .content("상세 내용")
                .category("네트워크")
                .build();

        PostResponse.PostCreateResponse response = postService.createPost(request, user.getId());

        // when
        PostResponse.PostDetailResponse detail = postService.getPostDetail(response.getId());

        // then
        assertThat(detail.getTitle()).isEqualTo("상세 조회 테스트");
        assertThat(detail.getContent()).isEqualTo("상세 내용");
        assertThat(detail.getUserId()).isEqualTo(user.getId());
        assertThat(detail.getQuestionId()).isEqualTo(question.getId());

    }

    @Test
    @DisplayName("게시글 저장")
    void createPost() {
        PostCreateRequest request = PostCreateRequest.builder()
                .title("새로운 게시글")
                .questionId(question.getId())
                .content("내용입니다.")
                .category("알고리즘")
                .build();

        // when
        PostResponse.PostCreateResponse response = postService.createPost(request, user.getId());

        // then
        assertThat(response.getTitle()).isEqualTo("새로운 게시글");
        assertThat(response.getContent()).isEqualTo("내용입니다.");
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getQuestionId()).isEqualTo(question.getId());

    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost() {
        // given
        PostCreateRequest createRequest = PostCreateRequest.builder()
                .title("원래 제목")
                .questionId(question.getId())
                .content("원래 내용")
                .category("네트워크")
                .build();

        PostCreateResponse createdPost = postService.createPost(createRequest, user.getId());

        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정 제목")
                .content("수정 내용")
                .questionId(question.getId())
                .category("기타")
                .build();

        // when
        PostUpdateResponse updatedPost = postService.updatePost(createdPost.getId(), updateRequest, user.getId());

        // then
        assertThat(updatedPost.getId()).isEqualTo(createdPost.getId());
        assertThat(updatedPost.getTitle()).isEqualTo("수정 제목");
        assertThat(updatedPost.getContent()).isEqualTo("수정 내용");
        assertThat(updatedPost.getCategory()).isEqualTo(Category.기타);
        assertThat(updatedPost.getQuestionId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void deletePost() {
        // given
        PostCreateRequest request = PostCreateRequest.builder()
                .title("삭제 테스트 제목")
                .questionId(question.getId())
                .content("내용")
                .category("기타")
                .build();
        PostCreateResponse response = postService.createPost(request, user.getId());
        Long postId = response.getId();

        // when
        postService.deletePost(postId, user.getId());

        // then
        boolean exists = postRepository.existsById(response.getId());
        assertThat(exists).isFalse();
    }

}
