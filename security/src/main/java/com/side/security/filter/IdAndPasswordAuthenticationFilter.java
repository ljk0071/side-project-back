package com.side.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.security.jwt.dto.SecurityDto;
import com.side.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static com.side.security.constant.FilterConstant.PASSWORD;
import static com.side.security.constant.FilterConstant.USER_ID;
import static com.side.security.util.ResponseUtil.createCookie;
import static com.side.security.util.ResponseUtil.createLoginSuccessResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class IdAndPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public IdAndPasswordAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            ObjectMapper objectMapper
    ) {
        // 로그인 경로 설정
        super("/api/sign/in");
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
            AuthenticationException, IOException {

        log.debug(" ## IdAndPasswordAuthenticationFilter attemptAuthentication ##");

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

        SecurityDto userDetails = (SecurityDto) authResult.getPrincipal();

        String userId = userDetails.getUserId();
        long userUniqueId = userDetails.getUniqueId();

        log.debug("인증 성공 userUniqueId: {}, userId: {}", userUniqueId, userId);

        response.addCookie(createCookie(
                true,
                request.getScheme(),
                AUTHORIZATION,
                jwtService.createAccessToken(userDetails.getUserId(), userDetails.getAuthorities()
                                                                                 .stream()
                                                                                 .map(Objects::toString)
                                                                                 .toList()),
                null
        ));

        String refreshToken = jwtService.createRefreshToken(userId);
        jwtService.createWhiteListForRefreshToken(userId, refreshToken);

        createLoginSuccessResponse(
                userUniqueId,
                userDetails.getUsername(),
                jwtService.createCsrfToken(userId),
                refreshToken,
                objectMapper,
                response
        );
    }
}
