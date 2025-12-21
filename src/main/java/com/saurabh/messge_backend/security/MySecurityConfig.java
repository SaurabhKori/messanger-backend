package com.saurabh.messge_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.saurabh.messge_backend.service.CustomUserDetailsService;

public class MySecurityConfig{
//	private final CustomUserDetailsService userDetailsService;
//	
//	  private  final JwtAuthFilter jwtAuthFilter;
//	  public MySecurityConfig(CustomUserDetailsService uds,JwtAuthFilter jwtAuthFilter) {
//	        this.userDetailsService = uds;
//	        this.jwtAuthFilter=jwtAuthFilter;
//	    }
//	@SuppressWarnings("removal")
//	public SecurityFilterChain filterChain(HttpSecurity http)  {
//		try {
//			http.csrf(csrf -> csrf.disable())
//            .cors().and()
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/login", "/userReg").permitAll()
//                .anyRequest().authenticated()
//            )
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////			http.csrf(csrf->csrf.disable())
////			.cors()
////			.and()
////			.authorizeHttpRequests()
////			.requestMatchers("/login").permitAll()
////			.anyRequest().authenticated()
////			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
////			.httpBasic(basic->basic.disable()).userDetailsService(userDetailsService)
//			
////			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////			
//			return http.build();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		
//		   
//	}
//	 @Bean
//	    public BCryptPasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
//	 @Bean
//	    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
//	      return http.getSharedObject(AuthenticationManagerBuilder.class)
//	                 .userDetailsService(userDetailsService)
//	                 .passwordEncoder(passwordEncoder())
//	                 .and().build();
//	    }
}
