package com.side.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("oauth2.discord")
public record DiscordOAuth2Properties(
        String clientId,
        String clientSecret,
        String redirectUri,
        String frontRedirectUri,
        List<String> scope,
        String state
) {
}