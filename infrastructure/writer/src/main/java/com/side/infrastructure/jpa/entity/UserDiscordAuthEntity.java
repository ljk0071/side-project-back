package com.side.infrastructure.jpa.entity;

import com.side.infrastructure.jpa.common.MetadataEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_discord_auth")
@EntityListeners(AuditingEntityListener.class)
public class UserDiscordAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("discord인증고유id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "discord_id", length = 20, nullable = false, unique = true)
    @Comment("discord사용자id(snowflake)")
    private String discordId;

    @Column(name = "discord_username", length = 32, nullable = false)
    @Comment("discord사용자명")
    private String discordUsername;

    @Column(name = "discord_discriminator", length = 4)
    @Comment("discord태그번호(#1234)")
    private String discordDiscriminator;

    @Column(name = "discord_global_name", length = 32)
    @Comment("discord글로벌표시명")
    private String discordGlobalName;

    @Column(name = "discord_email", length = 255)
    @Comment("discord이메일")
    private String discordEmail;

    @Column(name = "discord_avatar", length = 255)
    @Comment("discord아바타해시")
    private String discordAvatar;

    @Column(name = "discord_verified")
    @Comment("discord이메일인증여부")
    private Boolean discordVerified;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_unique_id", nullable = false)
    @Comment("유저고유id")
    private UserEntity user;

    @Embedded
    private MetadataEntity metadata;
}