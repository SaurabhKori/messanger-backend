package com.saurabh.messge_backend.dto;

public record FileAttachmentDTO(Long fileId, Long userId, String destination, String base64) {

}
