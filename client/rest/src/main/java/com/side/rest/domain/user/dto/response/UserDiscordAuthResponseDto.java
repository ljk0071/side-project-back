package com.side.rest.domain.user.dto.response;

import com.side.rest.domain.board.dto.response.MetadataResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDiscordAuthResponseDto {

    private Long id;
    private Long revision;
    private Long userUniqueId;
    private String discordId;
    private String discordUsername;
    private String discordDiscriminator;
    private String discordGlobalName;
    private String discordEmail;
    private String discordAvatar;
    private Boolean discordVerified;
    private MetadataResponseDto metadata;
}