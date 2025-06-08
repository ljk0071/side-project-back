package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Embedded
	private UserReactionEntity userReaction;
}
