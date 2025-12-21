package com.saurabh.messge_backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;

	@ManyToOne
	@JoinColumn(name = "sender_id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserLogin sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserLogin receiver;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	private String content;
	private String messageType; // text/image/file

	@ManyToOne
	@JoinColumn(name = "attachment_id")
	private FileAttachment attachment;

	private LocalDateTime createdAt;
	private LocalDateTime deliveredAt;
	private LocalDateTime seenAt;
}
