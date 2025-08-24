package com.side.security.jwt.service;

import com.side.domain.GenericClassToken;
import com.side.domain.memory.constants.RedisKeyNames;
import com.side.domain.memory.service.MemoryService;
import com.side.security.exception.InvalidTokenException;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.config.JwtProperties;
import com.side.security.jwt.enums.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final String TOKEN_TYPE = "token_type";
    private static final String ROLES = "roles";

    private final MemoryService memoryService;
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

    public String createAccessToken(String userId, List<String> roles) {
        return jwtbuilder(userId, jwtProperties.expirationTime(), JwtTokenType.ACCESS.name())
                .claim(ROLES, roles)
                .compact();
    }

    public String createRefreshToken(String userId) {
        return jwtbuilder(userId, jwtProperties.refreshExpirationTime(), JwtTokenType.REFRESH.name())
                .compact();
    }

    public void createWhiteListForRefreshToken(String userId, String refreshToken) {
        String key = RedisKeyNames.JWT_REFRESH_TOKEN + userId;
        memoryService.create(key, refreshToken, jwtProperties.refreshExpirationTime(), TimeUnit.SECONDS);
    }

    public String getRefreshTokenFromWhiteList(String userId) {
        return memoryService.find(RedisKeyNames.JWT_REFRESH_TOKEN + userId, String.class)
                            .orElseThrow(() -> new InvalidTokenException("refresh token이 없습니다.", true));
    }

    public String createCsrfToken(String userId) {
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
