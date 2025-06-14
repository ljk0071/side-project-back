package com.side.infrastructure.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "comment")
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("댓글 ID")
	private Long id;

	@Comment("댓글 내용")
	@Column(name = "contents", length = 500)
	private String contents;

	@Comment("게시판 ID")
	@Column(name = "board_id")
	private Long boardId;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "party_recruit_id")
	@Comment("파티 모집글 ID")
	private PartyRecruitEntity partyRecruit;

	// 부모 댓글 (대댓글인 경우에만 값 존재)
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	@Comment("부모 댓글 ID")
	private CommentEntity parentComment;

	// 자식 댓글들 (대댓글 목록)
	@OneToMany(mappedBy = "parentComment",
		cascade = {CascadeType.PERSIST, CascadeType.MERGE},
		fetch = FetchType.LAZY)
	private List<CommentEntity> childComments = new ArrayList<>();

	@Embedded
	private UserReactionEntity userReaction;

	@Embedded
	private MetadataEntity metadata;

	// 편의 메서드
	public void addChildComment(CommentEntity childComment) {
		childComments.add(childComment);
		childComment.setParentComment(this);
		childComment.setPartyRecruit(this.partyRecruit); // 같은 파티 모집글로 설정
	}

	public void removeChildComment(CommentEntity childComment) {
		childComments.remove(childComment);
		childComment.setParentComment(null);
	}

	// 최상위 댓글인지 확인
	public boolean isRootComment() {
		return parentComment == null;
	}

	// 대댓글인지 확인
	public boolean isReplyComment() {
		return parentComment != null;
	}

}
