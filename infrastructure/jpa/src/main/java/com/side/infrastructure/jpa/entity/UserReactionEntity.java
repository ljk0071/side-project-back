package com.side.infrastructure.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_reaction")
@EntityListeners(AuditingEntityListener.class)
public class UserReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("유저반응id")
    private Long id;

    @Column(name = "user_unique_id", nullable = false)
    @Comment("사용자id")
    private Long userUniqueId;

    @Column(name = "target_type", length = 20, nullable = false)
    @Comment("대상타입:notice,comment")
    private String targetType;

    @Column(name = "target_id", nullable = false)
    @Comment("대상id(notice_id또는comment_id)")
    private Long targetId;

    @Column(name = "reaction_type", length = 10, nullable = false)
    @Comment("반응타입:like,dislike")
    private String reactionType;

    @Column(name = "is_deleted", nullable = false)
    @Comment("취소여부")
    private Boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @Comment("반응일시")
    private Instant createdAt;

    @Column(name = "deleted_at")
    @Comment("취소일시")
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_unique_id", insertable = false, updatable = false)
    private UserEntity user;
}
