package com.side.rest.login.discord.controller;

import com.side.domain.AsyncUtil;
import com.side.domain.ExternalApiException;
import com.side.domain.model.*;
import com.side.rest.login.discord.dto.request.OAuth2RequestDto;
import com.side.security.config.DiscordOAuth2Properties;
import com.side.security.jwt.service.JwtService;
import com.side.usecase.user.UserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import static com.side.rest.mapper.UserMapper.UserMapper;
import static com.side.rest.util.RequestUtil.getDomain;
import static com.side.rest.util.RequestUtil.getServerUrl;
import static com.side.security.constant.FilterConstant.REFRESH_TOKEN;
import static com.side.security.constant.FilterConstant.X_CSRF_TOKEN;
import static com.side.security.util.ResponseUtil.createCookie;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/discord")
public class DiscordOAuth2Controller {

    private static final int MAX_AGE = 10000;
    private final UserUseCase userUseCase;
    private final JwtService jwtService;
    private final DiscordOAuth2Properties discordOAuth2Properties;

    @GetMapping("/redirect")
    public void handleDiscordCallback(
            OAuth2RequestDto dto,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String returnUrl = getDomain() + discordOAuth2Properties.frontRedirectUri();

        if (isInvalidRequest(returnUrl, dto, response)) {
            return;
        }

        DiscordInfoResponseDto responses;
        try {

            OAuth2ResponseDto tokenResponse = userUseCase.getAccessTokenFromDiscord(getServerUrl(), dto.getCode());

            responses = userUseCase.getUserInfoFromDiscord(tokenResponse.getAccessToken());

            try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {
                AsyncUtil.runAsync(() -> userUseCase.revokeAccessToken(tokenResponse.getAccessToken()), executors);
            }

        } catch (ExternalApiException e) {
            log.error("알 수 없는 디스코드 측 에러 발생 : {}", e.getMessage(), e);
            if (e.getMessage()
                 .contains("You are being rate limited for requesting too many tokens. Please try again later.")) {
                response.sendRedirect(returnUrl + "?error=tooManyRequests");
            } else {
                response.sendRedirect(returnUrl + "?error=unknown");
            }
            return;
        }

        DiscordInfoResponseDto.UserInfo discordUserInfo = responses.getUser();

        User user = userUseCase.findByDiscordId(discordUserInfo.getDiscordId())
                               .orElseGet(() -> {
                                   UserDiscordAuth discordAuth = UserMapper.toDomain(discordUserInfo);
                                   userUseCase.createFromDiscord(UserMapper.toDomain(discordAuth), discordAuth);

                                   return userUseCase.getByDiscordId(discordUserInfo.getDiscordId());
                               });

        String refreshToken = jwtService.createRefreshToken(user.userId());
        jwtService.createWhiteListForRefreshToken(user.userId(), refreshToken);

        List.of(
                    createCookie(
                            true,
                            AUTHORIZATION,
                            jwtService.createAccessToken(user.userId(), user.roles()
                                                                            .stream()
                                                                            .map(Role::code)
                                                                            .toList()),
                            request.getScheme(),
                            null
                    ),
                    createCookie(
                            false,
                            X_CSRF_TOKEN,
                            jwtService.createCsrfToken(user.userId()),
                            request.getScheme(),
                            MAX_AGE
                    ),
                    createCookie(
                            false,
                            REFRESH_TOKEN,
                            refreshToken,
                            request.getScheme(),
                            MAX_AGE
                    ))
            .forEach(response::addCookie);


        response.sendRedirect(String.format("%s?result=%s&userName=%s&userUniqueId=%s", returnUrl, Boolean.TRUE, user.name(), user.uniqueId()));
    }

    private boolean isInvalidRequest(String returnUrl, OAuth2RequestDto dto, HttpServletResponse response) throws IOException {

        if (!discordOAuth2Properties.state().equals(dto.getState())) {
            log.error("discord login 시도가 click-jacking 되었습니다 : {}", dto.getState());
            response.sendRedirect(getDomain() + "/404");
            return true;
        }

        if (dto.getError() != null) {
            if ("access_denied".equals(dto.getError())) {
                response.sendRedirect(returnUrl + "?error=canceledByUser");
                return true;
            }

            log.error("알 수 없는 에러가 발생 하였습니다 : {}", dto.getErrorDescription());
            response.sendRedirect(returnUrl + "?error=unknown");
            return true;
        }

        if (dto.getCode() == null) {
            log.error("code가 전달되지 않았습니다.");
            response.sendRedirect(returnUrl + "?error=codeNotFound");
            return true;
        }

        return false;
    }
}