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
@Table(name = "resume")
@EntityListeners(AuditingEntityListener.class)
public class ResumeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("이력서id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "user_unique_id", nullable = false)
    private Long userUniqueId;

    @Column(name = "status", length = 1, nullable = false)
    @Convert(converter = YesNoDeleteStatusConverter.class)
    @Comment("상태")
    private YesNoDeleteStatus status;

    @Column(name = "contents", nullable = false)
    @Comment("이력서 내용")
    private String contents;

    @Embedded
    private MetadataEntity metadata;
}