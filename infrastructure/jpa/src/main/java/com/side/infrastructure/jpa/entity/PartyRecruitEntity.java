package com.side.infrastructure.jpa.entity;

import com.side.domain.YesNoDeleteStatus;
import com.side.infrastructure.jpa.common.MetadataEntity;
import com.side.infrastructure.jpa.converter.YesNoDeleteStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "party_recruit")
@EntityListeners(AuditingEntityListener.class)
public class PartyRecruitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("파티 모집글 ID")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    private Long revision;

    @Embedded
    private ArticleEntity article;

    @Column(name = "user_unique_id", nullable = false)
    private Long userUniqueId;

    @Column(name = "max_members", nullable = false)
    @Comment("최대 모집 인원")
    private Integer maxMembers;

    @Column(name = "status", length = 1, nullable = false)
    @Convert(converter = YesNoDeleteStatusConverter.class)
    @Comment("상태")
    private YesNoDeleteStatus status;

    @Embedded
    private MetadataEntity metadata;
}
