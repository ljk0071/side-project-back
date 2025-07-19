package com.side.usecase.user;

import com.side.domain.HttpRequest;
import com.side.domain.exception.NotExistException;
import com.side.domain.model.DiscordInfoResponseDto;
import com.side.domain.model.OAuth2ResponseDto;
import com.side.domain.model.User;
import com.side.domain.model.UserDiscordAuth;
import com.side.domain.service.UserRoleService;
import com.side.domain.service.UserService;
import com.side.security.config.DiscordOAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final HttpRequest httpRequest;

    private final DiscordOAuth2Properties discordOAuth2Properties;

    @Transactional
    public void create(User user) {
        userService.create(user);
    }

    @Transactional
    public void createFromDiscord(User user, UserDiscordAuth userDiscordAuth) {

        long userUniqueId = userService.create(user.toBuilder()
                                                   .userId(userDiscordAuth.discordUsername())
                                                   .password(UUID.randomUUID().toString())
                                                   .name(userDiscordAuth.discordGlobalName())
                                                   .build());

        userRoleService.createNormalUser(userUniqueId);

        userService.createFromDiscord(userDiscordAuth.toBuilder()
                                                     .userUniqueId(userUniqueId)
                                                     .build());
    }

    @Transactional(readOnly = true)
    public Optional<User> findByDiscordId(String discordId) {
        return userService.findByDiscordId(discordId);
    }

    @Transactional(readOnly = true)
    public User getByDiscordId(String discordId) {
        return userService.findByDiscordId(discordId)
                          .orElseThrow(() -> new NotExistException("존재하지 않는 회원입니다."));
    }

    public OAuth2ResponseDto getAccessTokenFromDiscord(String serverUrl, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", serverUrl + discordOAuth2Properties.redirectUri());
        formData.add("scope", String.join("+", discordOAuth2Properties.scope()));
        formData.add("client_id", discordOAuth2Properties.clientId());
        formData.add("client_secret", discordOAuth2Properties.clientSecret());

        return httpRequest.post(
                "https://discord.com/api/oauth2/token",
                Map.of(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE),
                formData,
                OAuth2ResponseDto.class
        );
    }

    public DiscordInfoResponseDto getUserInfoFromDiscord(String accessToken) {
        return httpRequest.get("https://discord.com/api/oauth2/@me",
                Map.of(AUTHORIZATION, "Bearer " + accessToken),
                null,
                DiscordInfoResponseDto.class);
    }

    public void revokeAccessToken(String accessToken) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("token", accessToken);
        formData.add("client_id", discordOAuth2Properties.clientId());
        formData.add("client_secret", discordOAuth2Properties.clientSecret());

        httpRequest.post(
                "https://discord.com/api/oauth2/token/revoke",
                Map.of(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE),
                formData,
                String.class
        );
    }
}