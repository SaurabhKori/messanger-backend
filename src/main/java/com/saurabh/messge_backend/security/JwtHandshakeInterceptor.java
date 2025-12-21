package com.saurabh.messge_backend.security;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.saurabh.messge_backend.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtUtil jwtService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) {

//		String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
////        jwtService.extractUsername(token);
//		Integer userId = jwtService.extractUserId(token);
//		System.out.println("-------" + userId);
//		if (userId == null)
//			return false;
//		attributes.put("userId", userId);
//		return true;
		try {
			String token = extractToken(request);
			Integer userId = jwtService.extractUserId(token);

			attributes.put("user", userId.toString());
			System.out.println("✅ WS User authenticated: " + userId);
			return true;

		} catch (ExpiredJwtException ex) {
			System.out.println("❌ WebSocket JWT expired");
			return false; // clean reject
		}
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub

	}

	private String extractToken(ServerHttpRequest request) {

		// 1️⃣ Try Authorization header (preferred)
		List<String> authHeaders = request.getHeaders().get("Authorization");
		if (authHeaders != null && !authHeaders.isEmpty()) {
			String bearer = authHeaders.get(0);
			if (bearer.startsWith("Bearer ")) {
				return bearer.substring(7);
			}
		}

		// 2️⃣ Fallback: token from query param (SockJS uses this)
		URI uri = request.getURI();
		String query = uri.getQuery();

		if (query != null) {
			for (String param : query.split("&")) {
				if (param.startsWith("token=")) {
					return param.substring("token=".length());
				}
			}
		}

		// 3️⃣ If nothing found → reject
		throw new RuntimeException("JWT token not found in request");
	}

}
