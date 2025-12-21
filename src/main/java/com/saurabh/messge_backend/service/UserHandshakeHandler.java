package com.saurabh.messge_backend.service;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {
		String userId = (String) attributes.get("user");
		if (userId == null) {
			return null; // connection rejected
		}

		return new StompPrincipal(userId);
//		return new StompPrincipal(attributes.get("userId").toString());
	}
}
