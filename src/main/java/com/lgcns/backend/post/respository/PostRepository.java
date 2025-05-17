package com.lgcns.backend.post.respository;

import com.lgcns.backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
