package com.side.infrastructure.jpa.repository;

import com.side.domain.model.User;
import com.side.domain.model.UserDiscordAuth;
import com.side.domain.repository.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.Instant;

import static com.side.infrastructure.jpa.mapper.UserMapper.UserMapper;

@RequiredArgsConstructor
@Repository
public class UserJpaRepository implements UserWriter {

    private final UserJpaInterface repository;

    private final JdbcClient jdbcClient;

    private final PasswordEncoder passwordEncoder;

    @Override
    public long create(User user) {

        var entity = UserMapper.toEntity(user.toBuilder()
                                             .password(passwordEncoder.encode(user.password()))
                                             .build());

        return repository.save(entity).getUniqueId();
    }

    @Override
    public long createFromDiscord(UserDiscordAuth userDiscordAuth) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql("""
                          insert into user_discord_auth (revision,
                                                         user_unique_id,
                                                         discord_id,
                                                         discord_username,
                                                         discord_discriminator,
                                                         discord_global_name,
                                                         discord_email,
                                                         discord_avatar,
                                                         discord_verified,
                                                         created_at,
                                                         created_by)
                          values(:revision, :userUniqueId, :discordId, :discordUsername, :discordDiscriminator, :discordGlobalName, :discordEmail, :discordAvatar, :discordVerified, :createdAt, :createdBy)
                          """)
                  .param("revision", 0)
                  .param("userUniqueId", userDiscordAuth.userUniqueId())
                  .param("discordId", userDiscordAuth.discordId())
                  .param("discordUsername", userDiscordAuth.discordUsername())
                  .param("discordDiscriminator", userDiscordAuth.discordDiscriminator())
                  .param("discordGlobalName", userDiscordAuth.discordGlobalName())
                  .param("discordEmail", userDiscordAuth.discordEmail())
                  .param("discordAvatar", userDiscordAuth.discordAvatar())
                  .param("discordVerified", userDiscordAuth.discordVerified())
                  .param("createdAt", Instant.now())
                  .param("createdBy", 0)
                  .update(keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException("Discord 인증 정보 저장 후 생성된 키를 가져오는데 실패했습니다.");
        }

        return key.longValue();
    }
}
