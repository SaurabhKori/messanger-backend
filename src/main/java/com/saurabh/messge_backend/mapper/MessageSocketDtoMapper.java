package com.saurabh.messge_backend.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.saurabh.messge_backend.dto.MessageSocketDTO;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.Message;

@Component
public class MessageSocketDtoMapper {
	public static MessageSocketDTO toSocketDTO(Message m) {

		Object attachment = null;
		if (m.getAttachment() != null) {
			FileAttachment f = m.getAttachment();
			attachment = Map.of("file_id", f.getFile_id(), "file_name", f.getFile_name(), "file_type", f.getFile_type(),
					"fileurl", f.getFileurl());
		}

		return new MessageSocketDTO(m.getMessageId(), m.getContent(), m.getMessageType(), m.getCreatedAt(),
				m.getSender(), m.getReceiver(), attachment);
	}

}
