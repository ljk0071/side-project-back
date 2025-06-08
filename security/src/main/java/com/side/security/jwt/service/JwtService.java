package com.side.security.jwt.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.side.domain.GenericClassToken;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.config.JwtProperties;
import com.side.security.jwt.dto.SecurityDto;
import com.side.security.jwt.enums.JwtTokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

	private static final String TOKEN_TYPE = "token_type";
	private static final String ROLES = "roles";

	private final JwtProperties jwtProperties;

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public JwtBuilder jwtbuilder(String userId, long time, String tokenType) {
		return Jwts.builder()
				   .claim(TOKEN_TYPE, tokenType)
				   .subject(userId)
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis() + time))
				   .signWith(getSignInKey())
				   .header()
				   .type("jwt")
				   .and();
	}

	public String generateToken(SecurityDto userDetails) {
		return jwtbuilder(userDetails.getUserId(), jwtProperties.expirationTime(), JwtTokenType.ACCESS.name())
				   .claim(ROLES, userDetails.getAuthorities()
											.stream()
											.map(Objects::toString)
											.toList())
				   .compact();
	}

	public String generateRefreshToken(String userId) {
		return jwtbuilder(userId, jwtProperties.refreshExpirationTime(), JwtTokenType.REFRESH.name())
				   .compact();
	}

	public String generateCsrfToken(String userId) {
		return jwtbuilder(userId, jwtProperties.refreshExpirationTime(), JwtTokenType.CSRF.name())
				   .compact();
	}

	private Claims extractAllClaims(String token) throws MalformedJwtException {

		return Jwts.parser()
				   .verifyWith(getSignInKey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}

	public String extractUserId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public List<String> extractRoles(String token) {
		return extractClaim(token, claims -> claims.get(
			ROLES,
			new GenericClassToken<List<String>>() {}.getRawType())
		);
	}

	public JwtTokenType extractTokenType(String token) {
		String tokenType = extractClaim(token, claims -> claims.get(TOKEN_TYPE, String.class));
		return JwtTokenType.valueOf(tokenType.toUpperCase());
	}

	public JwtClaims getJwtClaims(String token) throws MalformedJwtException {
		Claims claims = extractAllClaims(token);
		return JwtClaims.builder()
						.userId(claims.getSubject())
						.tokenType(JwtTokenType.valueOf(claims.get(TOKEN_TYPE, String.class).toUpperCase()))
						.roles(claims.get(ROLES, new GenericClassToken<List<String>>() {}.getRawType()))
						.build();
	}
}
