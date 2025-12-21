package com.saurabh.messge_backend.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saurabh.messge_backend.dao.FileUploadDao;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.UserLogin;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.utils.EncrepytDecrypt;

@RestController
@CrossOrigin(origins = { "http://localhost:4200/", "*" })
public class FileUploadController {
	@Autowired
	private FileUploadDao filerepo;
	@Autowired
	EncrepytDecrypt encrypt;
//	private static StringBuffer UPLOAD_DIR = new StringBuffer("D:/uploads/");
	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping(value = "/uploadAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> uploadFile(@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "userId", required = true) Long userId,
			@RequestParam(value = "destination", required = true) String destination) {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.builder().message("File is required").success(false).build());
		}

		try {
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

			Path dir = Paths.get(uploadDir, destination);
			Files.createDirectories(dir);

			Path filePath = dir.resolve(fileName);
			file.transferTo(filePath); // 🔥 STREAMING (NO MEMORY LOAD)

			FileAttachment attachment = new FileAttachment();
			attachment.setFile_name(fileName);
			attachment.setFileurl("/files/" + destination + "/" + fileName);
			attachment.setUploadtime(LocalDateTime.now());
			attachment.setFile_type(file.getContentType()); // ✅ REQUIRED
			attachment.setDestination(destination);
			UserLogin user = new UserLogin();
			user.setUserId(userId);
			attachment.setUser_id(user);

			return filerepo.uploadFile(attachment);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder().message("Upload failed").success(false).build());
		}
	}

	@PostMapping("/getFileByDestination")
	private ResponseEntity<ApiResponse> getFileAttachmentByDestination(@RequestBody Map<String, String> payload) {
		System.out.println(payload.get("payload"));
		String decryptedData = encrypt.decryptAesIV(payload.get("payload")); // decrypt incoming payload

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		try {
			Map<String, Object> map = mapper.readValue(decryptedData, Map.class);

			if (!map.isEmpty() && map.containsKey("userid") && map.containsKey("destination")) {

				Long userId = Long.valueOf(map.get("userid").toString());
				List<FileAttachment> files = filerepo.getFilebyUserIdandDestination(userId,
						(String) map.get("destination"));
				System.out.println("FileAttachment: " + files);

				if (!files.isEmpty()) {
					FileAttachment file = files.get(0);

					file.setUserId((long) file.getUserid().getUserId());
					// Optional: set userid field null to avoid serialization issues
					file.setFileurl("http://10.50.116.99:8400" + file.getFileurl());

					// 🔴 Don't decrypt here. You should ENCRYPT before returning
					String data = encrypt.encryptAesIV(mapper.writeValueAsString(file));

					return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.ACCEPTED).success(true)
							.payload(data).message("Success").build());
				} else {
					return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.NOT_FOUND).success(false)
							.payload(null).message("No files found").build());
				}
			} else {
				return ResponseEntity.ok(ApiResponse.builder().status(HttpStatus.NOT_ACCEPTABLE).success(false)
						.payload(null).message("Invalid Data").build());
			}

		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message("Server Issue")
							.success(false).payload(null).build());
		}
	}

}
