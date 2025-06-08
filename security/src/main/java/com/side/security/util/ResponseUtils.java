package com.side.security.util;

import static com.side.security.constant.FilterConstant.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.memory.constants.RedisKeyNames;
import com.side.domain.memory.service.MemoryService;
import com.side.security.jwt.config.JwtProperties;
import com.side.security.jwt.dto.SecurityDto;
import com.side.security.jwt.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ResponseUtils {

	private final JwtService jwtService;
	private final JwtProperties jwtProperties;
	private final ObjectMapper objectMapper;
	private final MemoryService memoryService;

	public void addHttpOnlyCookie(
		String scheme,
		String key,
		String value,
		HttpServletResponse response
	) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath(ALL_PATH);
		cookie.setHttpOnly(true);
		if (HTTPS.equals(scheme)) {
			cookie.setSecure(true);
		}
		cookie.setAttribute(SAME_SITE, LAX);
		response.addCookie(cookie);
	}

	public void setSuccessJsonResponse(HttpServletResponse response) {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(APPLICATION_JSON.toString());
		response.setStatus(HttpServletResponse.SC_OK);
	}

	public void createLoginSuccessResponse(
		String csrfToken,
		String refreshToken,
		HttpServletResponse response
	) throws IOException {

		Map<String, Object> result = new HashMap<>();
		result.put("csrfToken", csrfToken);
		result.put("refreshToken", refreshToken);

		try (PrintWriter writer = response.getWriter()) {
			writer.write(objectMapper.writeValueAsString(result));
		}
	}

	public void doLoginSuccessAction(
		String userId,
		SecurityDto userDetails,
		HttpServletRequest request,
		HttpServletResponse response
	) throws IOException {

		// accessToken을 httpOnlyCookie로 전송
		String accessToken = jwtService.generateToken(userDetails);
		addHttpOnlyCookie(request.getScheme(), HttpHeaders.AUTHORIZATION, accessToken, response);

		// refreshToken을 redis에 저장
		String refreshToken = jwtService.generateRefreshToken(userId);
		String key = RedisKeyNames.JWT_REFRESH_TOKEN + userId;
		memoryService.create(key, refreshToken, jwtProperties.refreshExpirationTime(), TimeUnit.MILLISECONDS);

		// refreshToken과 csrfToken을 client에 반환
		setSuccessJsonResponse(response);
		String csrfToken = jwtService.generateCsrfToken(userId);
		createLoginSuccessResponse(csrfToken, refreshToken, response);
	}
}
