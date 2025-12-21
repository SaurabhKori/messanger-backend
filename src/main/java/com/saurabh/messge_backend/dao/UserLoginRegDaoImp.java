package com.saurabh.messge_backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saurabh.messge_backend.model.UserLogin;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.repo.UserLoginRegRepo;

@Service
public class UserLoginRegDaoImp implements UserLoginRegDao {
	@Autowired
	private UserLoginRegRepo userRepo;

	@Override
	public ResponseEntity<ApiResponse> userLogin(Long phone, String password) {

		return null;
	}

	@Override
	public ResponseEntity<ApiResponse> userRegistration(UserLogin userprofile) {
		// TODO Auto-generated method stub
		if (userRepo.existsByUserPhone(userprofile.getUserPhone()))
			return new ResponseEntity<>(ApiResponse.builder().message("Already Account Created by this Number")
					.success(false).status(HttpStatus.NOT_ACCEPTABLE).build(), HttpStatus.NOT_ACCEPTABLE);
		userRepo.save(userprofile);
		ApiResponse response = ApiResponse.builder().message("Success").status(HttpStatus.ACCEPTED).success(true)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	@Override
	public UserLogin findByUserPhone(String phone) {
		;
		return userRepo.findByUserPhone(phone).get();
	}

	@Override
	public UserLogin updateByUserId(UserLogin user) {
		UserLogin users = userRepo.findById(user.getUserId()).orElse(null);
		if (users != null) {
//			users.setAttachments(user.getAttachments());
			return userRepo.save(users);
		}
		return null;
	}

	@Override
	public UserLogin findByUserID(Long userid) {
		UserLogin user = userRepo.findById(userid).orElse(null);
		if (user != null) {
			return user;
		}
		return null;
	}

}
