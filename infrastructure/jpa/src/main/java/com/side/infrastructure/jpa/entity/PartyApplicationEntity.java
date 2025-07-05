package com.side.infrastructure.jpa.entity;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.infrastructure.jpa.common.MetadataEntity;
import com.side.infrastructure.jpa.converter.PartyApplicationStatusConverter;
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
@Table(name = "party_application")
@EntityListeners(AuditingEntityListener.class)
public class PartyApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("파티모집글과이력서매핑id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "status", length = 1, nullable = false)
    @Convert(converter = PartyApplicationStatusConverter.class)
    @Comment("지원상태:P(Pending),A(Accepted),R(Rejected),C(Canceled)")
    private PartyApplicationStatusTypeEnum status;

    @Column(name = "party_recruit_id")
    @Comment("파티모집글id")
    private long partyRecruitId;

    @Column(name = "resume_id")
    @Comment("지원서id")
    private long resumeId;

    @Embedded
    private MetadataEntity metadata;
}