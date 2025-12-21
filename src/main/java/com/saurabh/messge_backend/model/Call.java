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
@Table(name = "calls")
public class Call {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long callId;

    @ManyToOne
    @JoinColumn(name = "caller_id")
    private UserLogin caller;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserLogin receiver;

    private String callType; // voice/video
    private int duration; // in seconds
    private LocalDateTime createdAt;
    private String status; // missed/answered
}
