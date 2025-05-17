package com.lgcns.backend.user.repository;

import com.lgcns.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    
}