package com.saurabh.messge_backend.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.saurabh.messge_backend.dao.UserLoginRegDao;
import com.saurabh.messge_backend.model.UserLogin;
@Component
public class CustomUserDetailsService {
//public class CustomUserDetailsService implements  UserDetailsService{
//	@Autowired
//   UserLoginRegDao userRepository;
//
//	@Override
//	public UserDetails loadUserByUsername(String phoneOrUsername) throws UsernameNotFoundException {
//		 UserLogin user = userRepository.findByUserPhone(phoneOrUsername);
//		        // We give everyone a single default authority — you can add real roles later
//		        return new org.springframework.security.core.userdetails.User(
//		            user.getUserPhone(),
//		            user.getUser_password(),
//		            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//		            );
//	}

}
