package com.saurabh.messge_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurabh.messge_backend.dao.ContactDao;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.utils.EncrepytDecrypt;

@RestController
@CrossOrigin(origins = { "http://localhost:4200/", "*" })
public class ContactController {
	@Autowired
	private ContactDao contactDao;
	@Autowired
	private EncrepytDecrypt encrypt;

	@PostMapping("addContact")
	public ResponseEntity<ApiResponse> addContact(@RequestBody Map<String, String> payload) {

		try {
			String data = encrypt.decryptAesIV(payload.get("payload"));

			ObjectMapper objMapper = new ObjectMapper();
			Map<String, Object> value = objMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});

			Long userId = ((Number) value.get("user_id")).longValue();

			// ✅ phone is STRING → parse safely
			Long userPhone = Long.parseLong(value.get("userPhone").toString());

			return contactDao.saveContact(userId, userPhone);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponse.builder().message("Internal Server Error").success(false).build());
		}
	}

	@PostMapping("/getAllContact")
	public ResponseEntity<ApiResponse> getALLContactByUserId(@RequestBody Map<String, String> payload) {

		String data = encrypt.decryptAesIV(payload.get("payload"));
		ObjectMapper objMapper = new ObjectMapper();
		try {
			Map<String, Object> value = objMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});

			Number userIdNum = (Number) value.get("user_id");
			Long userId = userIdNum.longValue();
			return contactDao.getContactByUserId(userId);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(ApiResponse.builder().message(e.getMessage())
					.status(HttpStatus.INTERNAL_SERVER_ERROR).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
