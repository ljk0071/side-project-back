package com.side.infrastructure.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.side.infrastructure.jpa.common.MetadataEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Embedded
	private ArticleEntity article;

	@OneToMany(mappedBy = "partyRecruit", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	private List<CommentEntity> comments = new ArrayList<>();

	@Embedded
	private MetadataEntity metadata;

	// 댓글 추가
	public void addComment(CommentEntity comment) {
		comments.add(comment);
		comment.setPartyRecruit(this);
	}

	// 댓글 제거
	public void removeComment(CommentEntity comment) {
		comments.remove(comment);
		comment.setPartyRecruit(null);
	}
}
