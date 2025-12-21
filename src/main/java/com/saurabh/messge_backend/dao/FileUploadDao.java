package com.saurabh.messge_backend.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.payload.ApiResponse;

public interface FileUploadDao {
	public ResponseEntity<ApiResponse> uploadFile(FileAttachment file) throws JsonProcessingException;

	public FileAttachment getFilebyUserID(int userid);

	public List<FileAttachment> getFilebyUserIdandDestination(Long userid, String destination);
}
