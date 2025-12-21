package com.saurabh.messge_backend.dao;

import org.springframework.http.ResponseEntity;

import com.saurabh.messge_backend.payload.ApiResponse;

public interface ContactDao {
	public ResponseEntity<ApiResponse> saveContact(Long userid, Long phone_number);

	public ResponseEntity<ApiResponse> getContactByUserId(Long userid);
}
