package com.side.security.filter;

import static com.side.security.constant.FilterConstant.*;
import static com.side.security.service.SecurityHelper.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.side.security.exception.InvalidTokenException;
import com.side.security.exception.NotFoundTokenException;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.enums.JwtTokenType;
import com.side.security.jwt.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomCsrfFilter extends OncePerRequestFilter {

	private static final Set<String> STATE_MODIFIABLE_METHODS = Set.of("POST", "PATCH", "PUT");
	private static final String CSRF_REQUIRED_URIS_PREFIX = "/api/auth";
	private final JwtService jwtService;

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {

		return !isAuthEndpoint(request) && !isModifyingRequest(request);
	}

	private boolean isAuthEndpoint(HttpServletRequest request) {
		return request.getRequestURI().startsWith(CSRF_REQUIRED_URIS_PREFIX);
	}

	private boolean isModifyingRequest(HttpServletRequest request) {
		return STATE_MODIFIABLE_METHODS.contains(request.getMethod());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		log.debug("## CustomCsrfFilter doFilterInternal ##");

		final String token = Optional.ofNullable(request.getHeader(X_CSRF_TOKEN))
									 .orElseThrow(() -> new NotFoundTokenException("CSRF Token이 제공되지 않았습니다."));

		JwtClaims jwtClaims = jwtService.getJwtClaims(token);

		if (JwtTokenType.CSRF != jwtClaims.getTokenType() &&
				getAuthenticatedUser().userId() != null &&
				!Objects.equals(getAuthenticatedUser().userId(), jwtClaims.getUserId())) {

			throw new InvalidTokenException("CSRF Token이 아닙니다.");
		}

		filterChain.doFilter(request, response);
	}

}
