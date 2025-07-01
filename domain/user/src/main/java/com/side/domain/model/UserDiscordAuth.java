package com.side.domain.model;

import com.side.domain.Metadata;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserDiscordAuth(
        Long id,
        Long revision,
        Long userUniqueId,
        String discordId,
        String discordUsername,
        String discordDiscriminator,
        String discordGlobalName,
        String discordEmail,
        String discordAvatar,
        Boolean discordVerified,
        Metadata metadata
) {
}