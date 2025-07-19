package com.side.infrastructure.jpa.entity;

import com.side.domain.YesNoDeleteStatus;
import com.side.infrastructure.jpa.common.MetadataEntity;
import com.side.infrastructure.jpa.converter.YesNoDeleteStatusConverter;
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
@Table(name = "user_role",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_role_role_id_user_unique_id",
                columnNames = {"role_id", "user_unique_id"}))
@EntityListeners(AuditingEntityListener.class)
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("유저역할id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "status", length = 1, nullable = false)
    @Convert(converter = YesNoDeleteStatusConverter.class)
    @Comment("상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)")
    private YesNoDeleteStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @Comment("역할id")
    private RoleEntity role;

    @Column(name = "user_unique_id", nullable = false)
    @Comment("사용자id")
    private Long userUniqueId;

    @Embedded
    private MetadataEntity metadata;
}
