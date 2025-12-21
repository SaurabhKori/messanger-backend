package com.saurabh.messge_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.saurabh.messge_backend.security.JwtHandshakeInterceptor;
import com.saurabh.messge_backend.service.UserHandshakeHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
	private final UserHandshakeHandler userHandshakeHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setHandshakeHandler(userHandshakeHandler) // 🔥 THIS
				.addInterceptors(jwtHandshakeInterceptor).setAllowedOriginPatterns("*").withSockJS();

//		registry.addEndpoint("/ws").addInterceptors(jwtInterceptor) // ✅ BOOLEAN interceptor
//				.setHandshakeHandler(new UserHandshakeHandler()) // ✅ Principal here
//				.setAllowedOriginPatterns("*").withSockJS().setSessionCookieNeeded(false);
//		;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
		registry.enableSimpleBroker("/queue", "/topic");
	}

//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
//	}
//
//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry registry) {
//		registry.enableSimpleBroker("/queue", "/topic");
//		registry.setUserDestinationPrefix("/user");
//		registry.setApplicationDestinationPrefixes("/app");
//	}
}
