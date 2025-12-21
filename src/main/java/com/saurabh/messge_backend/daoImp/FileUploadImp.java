package com.saurabh.messge_backend.daoImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saurabh.messge_backend.dao.FileUploadDao;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.repo.FileAttachmentRepo;
import com.saurabh.messge_backend.utils.EncrepytDecrypt;

import jakarta.transaction.Transactional;

@Repository
public class FileUploadImp implements FileUploadDao {
	@Autowired
	private FileAttachmentRepo fileRepo;
	@Autowired
	private EncrepytDecrypt encrypt;

	@Override
	@Transactional
	public ResponseEntity<ApiResponse> uploadFile(FileAttachment file) throws JsonProcessingException {
		if (file.getDestination().equalsIgnoreCase("profile")) {
			fileRepo.deleteByUserid_UserIdAndDestination(file.getUserid().getUserId(), file.getDestination());
		}
		FileAttachment files = fileRepo.save(file);
		if (files != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			String data = encrypt.encryptAesIV(mapper.writeValueAsString(files));
			ApiResponse response = ApiResponse.builder().message("Success").status(HttpStatus.ACCEPTED).payload(data)
					.success(true).build();
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(
				ApiResponse.builder().message("Not Success").status(HttpStatus.NOT_ACCEPTABLE).success(false).build(),
				HttpStatus.NOT_ACCEPTABLE);
	}

	@Override
	public FileAttachment getFilebyUserID(int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileAttachment> getFilebyUserIdandDestination(Long userid, String destination) {
		List<FileAttachment> files = this.fileRepo.findByUserid_UserIdAndDestination(userid, destination);
		if (!files.isEmpty()) {
			return files;
		}
		return null;
	}

}
