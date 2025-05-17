package com.lgcns.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

    
}