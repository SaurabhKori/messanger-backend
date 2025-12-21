package com.saurabh.messge_backend.dto;

import java.time.LocalDateTime;

import com.saurabh.messge_backend.model.UserLogin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSocketDTO {

	private Long messageId;
	private String content;
	private String messageType;
	private LocalDateTime createdAt;

	private UserLogin sender;
	private UserLogin receiver;

	private Object attachment; // simple JSON
}
