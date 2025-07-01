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
    @Comment("지원상태:P(Pending),A(Accepted),R(Rejected),C(Canceled)")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_recruit_id", nullable = false)
    @Comment("파티모집글id")
    private PartyRecruitEntity partyRecruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @Comment("지원서id")
    private ResumeEntity resume;

    @Embedded
    private MetadataEntity metadata;
}