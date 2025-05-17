package com.lgcns.backend.csanswer.domain;

import java.time.LocalDateTime;

import com.lgcns.backend.user.domain.User;
import com.lgcns.backend.csquestion.domain.CSQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "question_id", nullable = false)
    private CSQuestion csQuestion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
