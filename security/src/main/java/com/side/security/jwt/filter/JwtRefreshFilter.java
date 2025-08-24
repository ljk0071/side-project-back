package com.side.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.domain.model.Role;
import com.side.domain.model.User;
import com.side.domain.service.UserService;
import com.side.security.exception.InvalidTokenException;
import com.side.security.jwt.claims.JwtClaims;
import com.side.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.side.security.constant.FilterConstant.REFRESH_TOKEN;
import static com.side.security.util.ResponseUtil.createCookie;
import static com.side.security.util.ResponseUtil.createLoginSuccessResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

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

        String refreshTokenInRedis = jwtService.getRefreshTokenFromWhiteList(userId);

        if (!refreshToken.equals(refreshTokenInRedis)) {
            throw new InvalidTokenException("만료된 refresh token입니다.", true);
        }

        User user = userService.getByUserId(userId);

        log.debug("재인증 성공 userUniqueId: {}, userId: {}", user.uniqueId(), userId);

        response.addCookie(createCookie(
                true,
                AUTHORIZATION,
                jwtService.createAccessToken(user.userId(), user.roles()
                                                                .stream()
                                                                .map(Role::code)
                                                                .toList()),
                request.getScheme(),
                null
        ));

        refreshToken = jwtService.createRefreshToken(userId);
        jwtService.createWhiteListForRefreshToken(userId, refreshToken);

        createLoginSuccessResponse(
                user.uniqueId(),
                user.name(),
                jwtService.createCsrfToken(userId),
                refreshToken,
                objectMapper,
                response
        );
    }
}
