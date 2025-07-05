package com.side.infrastructure.jpa.entity;

import com.side.domain.YesNoDeleteStatus;
import com.side.infrastructure.jpa.common.MetadataEntity;
import com.side.infrastructure.jpa.converter.YesNoDeleteStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "notice")
@EntityListeners(AuditingEntityListener.class)
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("공지사항id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Embedded
    private ArticleEntity article;

    @Column(name = "view_count", nullable = false)
    @Comment("조회수")
    private Long viewCount = 0L;

    @Column(name = "status", length = 1, nullable = false)
    @Convert(converter = YesNoDeleteStatusConverter.class)
    @Comment("상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)")
    private YesNoDeleteStatus status;

    @Embedded
    private MetadataEntity metadata;

    // 양방향 연관관계
    @OneToMany(mappedBy = "notice", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<NoticeCommentEntity> comments = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addComment(NoticeCommentEntity comment) {
        comments.add(comment);
        comment.setNotice(this);
    }

    public void removeComment(NoticeCommentEntity comment) {
        comments.remove(comment);
        comment.setNotice(null);
    }
}
