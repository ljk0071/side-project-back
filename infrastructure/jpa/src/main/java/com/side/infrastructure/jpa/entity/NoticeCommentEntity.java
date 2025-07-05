package com.side.infrastructure.jpa.entity;

import com.side.domain.YesNoDeleteStatus;
import com.side.infrastructure.jpa.common.MetadataEntity;
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
@Table(name = "notice_comment")
@EntityListeners(AuditingEntityListener.class)
public class NoticeCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("공지사항댓글id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "contents", length = 255, nullable = false)
    @Comment("댓글내용")
    private String contents;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("상태:Y(Yes/활성),N(No/비활성),D(Deleted/삭제)")
    private YesNoDeleteStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    @Comment("공지사항id")
    private NoticeEntity notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    @Comment("부모댓글id")
    private NoticeCommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<NoticeCommentEntity> childComments = new ArrayList<>();

    @Embedded
    private MetadataEntity metadata;

    // 연관관계 편의 메서드
    public void addChildComment(NoticeCommentEntity childComment) {
        childComments.add(childComment);
        childComment.setParentComment(this);
    }

    public void removeChildComment(NoticeCommentEntity childComment) {
        childComments.remove(childComment);
        childComment.setParentComment(null);
    }
}