package com.saurabh.messge_backend.daoImp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saurabh.messge_backend.dto.MessageDTO;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.Message;
import com.saurabh.messge_backend.repo.FileAttachmentRepo;
import com.saurabh.messge_backend.repo.MessageRepository;
import com.saurabh.messge_backend.repo.UserLoginRegRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository repo;
	private final UserLoginRegRepo userRepo;
	private final FileAttachmentRepo attachmentRepo;

	public Message savePrivate(Long senderId, MessageDTO dto) {

		Message m = new Message();
		m.setSender(userRepo.findById(senderId).orElse(null));
		m.setReceiver(userRepo.findById(dto.receiverId).orElse(null));
		m.setContent(dto.content);
		m.setMessageType(dto.messageType);
		m.setCreatedAt(LocalDateTime.now());
		if (dto.getAttachmentId() != null) {
			FileAttachment fa = attachmentRepo.findById(dto.getAttachmentId().intValue()).orElseThrow();
			m.setAttachment(fa);
		}

		return repo.save(m);
	}

	public List<Message> getPrivateChat(Long userId, Long contactId) {
		return repo.getPrivateChat(userId, contactId);
	}

	@Transactional
	public void markDelivered(Long messageId) {
		repo.markDelivered(messageId, LocalDateTime.now());
	}

	public Message getById(Long id) {
		return repo.findById(id).orElseThrow();
	}

	@Transactional
	public Message markSeen(Long messageId) {
		repo.markSeen(messageId, LocalDateTime.now());
		return repo.findById(messageId).orElseThrow();
	}

	@Transactional
	public void markHistoryDelivered(Long senderId, Long receiverId) {
		repo.markHistoryDelivered(senderId, receiverId, LocalDateTime.now());
	}

//    public Message saveGroupMessage(MessageDTO dto) {
//        Message msg = new Message();
//        msg.setSender(userRepo.findById(dto.senderId).orElseThrow());
//        msg.setGroup(groupRepo.findById(dto.groupId).orElseThrow());
//        msg.setContent(dto.content);
//        msg.setMessageType(dto.messageType);
//        msg.setCreatedAt(LocalDateTime.now());
//
//        return messageRepo.save(msg);
//    }
}
