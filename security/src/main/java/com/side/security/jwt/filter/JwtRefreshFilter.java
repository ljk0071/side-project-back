package com.side.security.jwt.filter;

import static com.side.security.constant.FilterConstant.*;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.side.domain.memory.constants.RedisKeyNames;
import com.side.domain.memory.service.MemoryService;
import com.side.security.exception.InvalidTokenException;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.dto.SecurityDto;
import com.side.security.jwt.service.JwtService;
import com.side.security.service.SecurityService;
import com.side.security.util.ResponseUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final MemoryService memoryService;
	private final SecurityService securityService;
	private final ResponseUtils responseUtils;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws IOException {

		log.debug("## JwtRefreshFilter doFilterInternal ##");

		String refreshToken = request.getHeader(REFRESH_TOKEN);

		JwtClaims jwtClaims = jwtService.getJwtClaims(refreshToken);

		String userId = jwtClaims.getUserId();

		String refreshTokenInRedis = memoryService.find(RedisKeyNames.JWT_REFRESH_TOKEN + userId, String.class)
												  .orElseThrow(() -> new InvalidTokenException("refresh token이 없습니다."));

		if (!refreshToken.equals(refreshTokenInRedis)) {
			throw new InvalidTokenException("만료된 refresh token입니다.");
		}

		SecurityDto userDetails = securityService.loadUserByUsername(userId);

		log.debug("재인증 성공 uniqueId: {}, userId: {}", userDetails.getUniqueId(), userId);

		responseUtils.doLoginSuccessAction(
			userId,
			userDetails,
			request,
			response
		);
	}
}
