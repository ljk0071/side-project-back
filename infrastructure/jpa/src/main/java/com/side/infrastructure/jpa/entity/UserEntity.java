package com.side.infrastructure.jpa.entity;

import com.side.domain.enums.UserStatus;
import com.side.domain.enums.UserType;
import com.side.infrastructure.jpa.common.MetadataEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user")
@DynamicInsert
@DynamicUpdate
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id")
    private Long uniqueId;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Comment("비밀번호")
    private String password;

    @Comment("이름")
    @Column(length = 50)
    private String name;

    @Column(name = "password_updated_at")
    @Comment("패스워드변경일시")
    private Instant passwordUpdatedAt;

    @Comment("이메일")
    private String email;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Comment("타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @Comment("설명")
    @Column(name = "description")
    private String description;

    @Embedded
    private MetadataEntity metadata;

    @Transient
    private List<RoleEntity> roles;
}
