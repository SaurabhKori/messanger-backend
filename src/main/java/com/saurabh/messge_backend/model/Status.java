package com.saurabh.messge_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "statuses")
public class Status {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserLogin user;

    private String content;

    @ManyToOne
    @JoinColumn(name = "attachment_id")
    private FileAttachment attachment;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
