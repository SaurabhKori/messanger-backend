package com.saurabh.messge_backend.dto;

import lombok.Data;

@Data
public class MessageDTO {
	public Long messageId;
	public Long receiverId;
	public Long groupId;
	public String content;
	public String messageType;
	public Long attachmentId;
}
