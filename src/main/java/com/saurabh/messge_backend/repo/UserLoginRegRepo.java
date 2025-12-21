package com.saurabh.messge_backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saurabh.messge_backend.model.UserLogin;

@Repository
public interface UserLoginRegRepo extends JpaRepository<UserLogin, Long> {

	public Optional<UserLogin> findByUserPhone(String userPhone);

	boolean existsByUserPhone(String userPhone);
//	boolean existsByUserName()
}
