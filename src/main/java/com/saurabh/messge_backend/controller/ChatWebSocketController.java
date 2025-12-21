package com.saurabh.messge_backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.saurabh.messge_backend.daoImp.MessageService;
import com.saurabh.messge_backend.dto.MessageDTO;
import com.saurabh.messge_backend.dto.MessageSocketDTO;
import com.saurabh.messge_backend.mapper.MessageSocketDtoMapper;
import com.saurabh.messge_backend.model.Message;
import com.saurabh.messge_backend.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = { "http://localhost:4200/", "*" })
@RequiredArgsConstructor
public class ChatWebSocketController {

	private final SimpMessagingTemplate template;
	private final MessageService service;
	private final JwtUtil jwtUtil;
	private final MessageSocketDtoMapper dtoMapper;

	@MessageMapping("/chat.private")
	public void privateChat(MessageDTO dto, Principal principal) {

		if (principal == null) {
			throw new IllegalStateException("❌ WebSocket Principal is NULL " + dto);
		}

		Long senderId = Long.valueOf(principal.getName());

		Message saved = service.savePrivate(senderId, dto);

		// Receiver
		template.convertAndSendToUser(dto.receiverId.toString(), "/queue/messages", dtoMapper.toSocketDTO(saved));

		// Sender (self echo like WhatsApp)
		template.convertAndSendToUser(senderId.toString(), "/queue/messages", dtoMapper.toSocketDTO(saved));
		// 🔥 MARK DELIVERED
		service.markDelivered(saved.getMessageId());

		Message deliveredMsg = service.getById(saved.getMessageId());

		// notify sender about delivery
		template.convertAndSendToUser(senderId.toString(), "/queue/status", deliveredMsg);
	}

	@GetMapping("/chat/history/{contactId}")
	public List<MessageSocketDTO> getChatHistory(@PathVariable Long contactId,
			@RequestHeader("Authorization") String token) {

		Long userId = (long) jwtUtil.extractUserId(token.substring(7));

		// 🔥 mark all old messages as delivered
		service.markHistoryDelivered(contactId, userId);

		List<MessageSocketDTO> messages = service.getPrivateChat(userId, contactId).stream()
				.map(MessageSocketDtoMapper::toSocketDTO).toList();
		return messages;
	}

//	@MessageMapping("/chat.typing")
//	public void typing(TypingDTO dto) {
//		template.convertAndSendToUser(dto.receiverId().toString(), "/queue/typing", dto);
//	}
	@MessageMapping("/chat.typing")
	public void typing(Map<String, Object> payload, Principal principal) {

		System.out.println("🔥 BACKEND typing hit: " + payload);

		Long senderId = Long.valueOf(principal.getName());
		Long receiverId = Long.valueOf(payload.get("receiverId").toString());
		Boolean typing = (Boolean) payload.get("typing");

		template.convertAndSendToUser(receiverId.toString(), "/queue/typing",
				Map.of("senderId", senderId, "typing", typing));
	}

	@MessageMapping("/chat.seen")
	public void seen(MessageDTO dto, Principal principal) {

		Message msg = service.markSeen(dto.getMessageId());

		template.convertAndSendToUser(String.valueOf(msg.getSender().getUserId()), "/queue/status", msg);
	}

}
