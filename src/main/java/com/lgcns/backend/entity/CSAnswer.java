package com.lgcns.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class CSAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String feedback;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @ManyToOne
    @Column(nullable = false)
    private CSQuestion csQuestion;

    @ManyToOne
    @Column(nullable = false)
    private User user;
}
