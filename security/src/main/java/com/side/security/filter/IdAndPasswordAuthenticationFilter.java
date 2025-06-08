package com.side.security.filter;

import static com.side.security.constant.FilterConstant.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.security.jwt.dto.SecurityDto;
import com.side.security.util.ResponseUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdAndPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final AuthenticationManager authenticationManager;
	private final ObjectMapper objectMapper;
	private final ResponseUtils responseUtils;

	public IdAndPasswordAuthenticationFilter(
		AuthenticationManager authenticationManager,
		ObjectMapper objectMapper,
		ResponseUtils responseUtils
	) {
		// 로그인 경로 설정
		super("/api/sign/in");
		this.authenticationManager = authenticationManager;
		this.objectMapper = objectMapper;
		this.responseUtils = responseUtils;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException, IOException {

		log.debug(" ## IdAndPasswordAuthenticationFilter attemptAuthentication ##");

		// 1 쿠키를 가져온다
		request.getCookies();

		String a = String.valueOf(Arrays.stream(request.getCookies())
										.map(cookie -> "JSESSIONID".equals(cookie.getName()))
										.findAny()
										.orElseThrow());

		Map<String, Object> authorizationMap = new HashMap<>();

		// 2. 서버에서는 일단 사용자(익명 포함) 오면 sessionId라는 cookie를 브라우저에 저장시켜서
		// 이 사용자의 요청을 다 구별할 수 있게 하는데
		authorizationMap.put("JSESSIONID", "83F3586C08040BE0B9339CBACC56DB85.jvm1;userId=oth5027;role=ROLE_USER;");

		// 3. 서버간의 기본적으로 알 수가 없어

		try (InputStream inputStream = request.getInputStream()) {

			// JSON 요청 본문 읽기
			Map<String, String> credentials = objectMapper.readValue(inputStream, new TypeReference<>() {});

			String userId = credentials.get(USER_ID);
			String password = credentials.get(PASSWORD);

			log.debug("로그인 시도 userId: {}", userId);

			// 인증 토큰 생성
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password);

			// 인증 시도
			return authenticationManager.authenticate(authToken);
		}
	}

	@Override
	protected void successfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication authResult
	) throws IOException {

		log.debug(" ## IdAndPasswordAuthenticationFilter successfulAuthentication ##");

		SecurityDto userDetails = (SecurityDto)authResult.getPrincipal();

		String userId = userDetails.getUserId();

		log.debug("인증 성공 uniqueId: {}, userId: {}", userDetails.getUniqueId(), userId);

		responseUtils.doLoginSuccessAction(
			userId,
			userDetails,
			request,
			response
		);
	}
}
