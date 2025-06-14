package com.side.security.jwt.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.side.domain.enums.UserStatus;
import com.side.domain.model.Role;
import com.side.domain.model.User;
import com.side.domain.service.UserService;
import com.side.security.exception.NotFoundTokenException;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.config.AuthenticationDetailsSource;
import com.side.security.jwt.enums.JwtTokenType;
import com.side.security.jwt.service.JwtService;
import com.side.security.service.RoleHierarchyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final RoleHierarchyService roleHierarchyService;
	private final UserService userService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws IOException, ServletException {

		log.debug("## JwtAuthenticationFilter doFilterInternal ##");

		final String accessToken = extractToken(request);

		if (!StringUtils.hasText(accessToken)) {
			filterChain.doFilter(request, response);
			return;
		}

		JwtClaims jwtClaims = jwtService.getJwtClaims(accessToken);

		String userId = jwtClaims.getUserId();
		Collection<String> roles = jwtClaims.getRoles();
		JwtTokenType tokenType = jwtClaims.getTokenType();

		if (JwtTokenType.ACCESS != (tokenType)) {
			log.error("[Not Access Token] token : {}, userId : {}, tokenType : {}", accessToken, userId, tokenType);
			filterChain.doFilter(request, response);
			return;
		}

		User user = userService.findByUserId(userId);

		if (UserStatus.ACTIVE != user.status()) {
			log.error("[Not ACTIVE USER] token : {}, userId : {}", accessToken, userId);
			filterChain.doFilter(request, response);
			return;
		}

		Collection<String> userRoleList = user.roles().stream().map(Role::id).toList();

		if (!isValidateUserRoles(roles, userRoleList)) {

			log.error("[Invalid Token Roles] token : {}, userId : {}, role : {}, userRole : {}",
				accessToken,
				userId,
				roles,
				userRoleList
			);

			filterChain.doFilter(request, response);
			return;
		}

		Collection<GrantedAuthority> authorities = roleHierarchyService.getGrantedAuthorities(roles);

		log.debug("authorities : {}", authorities);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			user,
			null,
			authorities
		);

		authentication.setDetails(new AuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private boolean isValidateUserRoles(Collection<String> tokenRoles, Collection<String> userRoles) {
		// Guard clauses
		if (tokenRoles == null || userRoles == null) {
			return false;
		}

		if (tokenRoles.size() != userRoles.size()) {
			return false;
		}

		// Set 변환은 한 번만
		return new HashSet<>(tokenRoles).equals(new HashSet<>(userRoles));
	}

	private String extractToken(HttpServletRequest request) {
		return Optional.ofNullable(extractAccessTokenFromCookie(request))
					   .or(() -> Optional.ofNullable(extractAccessTokenFromHeader(request)))
					   .orElseThrow(() -> new NotFoundTokenException("Access Token을 찾을 수 없습니다"));
	}

	private String extractAccessTokenFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		return Arrays.stream(cookies)
					 .filter(cookie -> HttpHeaders.AUTHORIZATION.equals(cookie.getName()))
					 .map(Cookie::getValue)
					 .findAny()
					 .orElse(null);
	}

	private String extractAccessTokenFromHeader(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}
}
