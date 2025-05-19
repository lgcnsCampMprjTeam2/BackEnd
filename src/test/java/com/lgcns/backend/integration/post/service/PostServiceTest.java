package com.lgcns.backend.integration.post.service;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("게시글 통합 테스트")
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

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
        //given
        postRepository.save(Post.builder()
                .title("제목1")
                .content("내용1")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());
        postRepository.save(Post.builder()
                .title("제목2")
                .content("내용2")
                .category(Category.알고리즘)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());

        //when
        List<Post> all = postRepository.findAll();

        //then
        assertThat(all).hasSize(2);

    }

    @Test
    @DisplayName("게시글 상세 조회")
    public void getPostDetail() {
        //given
        Post post = postRepository.save(Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());

        //when
        Optional<Post> foundPost = postRepository.findById(post.getId());

        //then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo(post.getTitle());

    }

    @Test
    @DisplayName("게시글 저장")
    public void createPost() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build();

        //when
        Post savedPost = postRepository.save(post);

        //then
        assertThat(savedPost.getId()).isEqualTo(post.getId());
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getContent()).isEqualTo(post.getContent());
        assertThat(savedPost.getCategory()).isEqualTo(post.getCategory());
        assertThat(savedPost.getCreatedAt()).isEqualTo(post.getCreatedAt());
        assertThat(savedPost.getUser()).isEqualTo(post.getUser());
        assertThat(savedPost.getCsQuestion()).isEqualTo(post.getCsQuestion());

        assertThat(savedPost.getContent()).isEqualTo("내용");

    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost() {
        //given
        Post post = postRepository.save(Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());

        CSQuestion updatedQuestion = csQuestionRepository.save(CSQuestion.builder()
                .category(Category.네트워크)
                .content("질문")
                .createdAt(LocalDateTime.now())
                .build());

        //when

        post.updatePost(Category.기타,"수정 제목","수정 내용", updatedQuestion);
        Post updatedPost = postRepository.save(post);

        //then
        assertThat(updatedPost.getId()).isEqualTo(post.getId());
        assertThat(updatedPost.getTitle()).isEqualTo("수정 제목");
        assertThat(updatedPost.getContent()).isEqualTo("수정 내용");
        assertThat(updatedPost.getCategory()).isEqualTo(Category.기타);
        assertThat(updatedPost.getCsQuestion().getId()).isEqualTo(updatedQuestion.getId());


    }

    @Test
    @DisplayName("게시글 삭제")
    public void deletePost()

    {
        //given
        Post post = postRepository.save(Post.builder()
                .title("제목")
                .content("내용")
                .category(Category.네트워크)
                .createdAt(LocalDateTime.now())
                .user(user)
                .csQuestion(question)
                .build());

        //when
        postRepository.delete(post);

        //then
        boolean exists = postRepository.existsById(post.getId());
        assertThat(exists).isFalse();

    }

}
