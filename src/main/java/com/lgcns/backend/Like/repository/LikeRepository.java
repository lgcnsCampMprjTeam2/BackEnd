package com.lgcns.backend.Like.repository;

import com.lgcns.backend.Like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
