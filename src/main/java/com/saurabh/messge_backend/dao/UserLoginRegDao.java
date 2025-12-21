package com.saurabh.messge_backend.dao;

import org.springframework.http.ResponseEntity;

import com.saurabh.messge_backend.model.UserLogin;
import com.saurabh.messge_backend.payload.ApiResponse;

public interface UserLoginRegDao {
	public ResponseEntity<ApiResponse> userRegistration(UserLogin userprofile);

	public ResponseEntity<ApiResponse> userLogin(Long phone, String password);

	public UserLogin findByUserPhone(String phone);

	public UserLogin updateByUserId(UserLogin user);

	public UserLogin findByUserID(Long userid);
}
