package com.side.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DiscordInfoResponseDto {

    @JsonProperty("application")
    private ApplicationDto application;

    @JsonProperty("expires")
    private String expires;

    @JsonProperty("scopes")
    private String[] scopes;

    @JsonProperty("user")
    private UserInfo user;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ApplicationDto {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("bot")
        private UserInfo bot;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class UserInfo {
        @JsonProperty("id")
        private String discordId;

        @JsonProperty("username")
        private String discordUsername;

        @JsonProperty("discriminator")
        private String discordDiscriminator;

        @JsonProperty("global_name")
        private String discordGlobalName;

        @JsonProperty("avatar")
        private String discordAvatar;

        @JsonProperty("bot")
        private Boolean bot;

        @JsonProperty("system")
        private Boolean system;

        @JsonProperty("mfa_enabled")
        private Boolean mfaEnabled;

        @JsonProperty("banner")
        private String banner;

        @JsonProperty("accent_color")
        private Integer accentColor;

        @JsonProperty("locale")
        private String locale;

        @JsonProperty("verified")
        private Boolean discordVerified;

        @JsonProperty("email")
        private String discordEmail;

        @JsonProperty("flags")
        private Integer flags;

        @JsonProperty("premium_type")
        private Integer premiumType;

        @JsonProperty("public_flags")
        private Integer publicFlags;

        @JsonProperty("avatar_decoration_data")
        private Object avatarDecorationData;

        @JsonProperty("collectibles")
        private Object collectibles;

        @JsonProperty("primary_guild")
        private Object primaryGuild;
    }
}
