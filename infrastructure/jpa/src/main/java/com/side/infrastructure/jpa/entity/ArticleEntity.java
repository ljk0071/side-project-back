package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class ArticleEntity {

	@Comment("게시글 제목")
	@Column(name = "title")
	private String title;

	@Comment("게시글 내용")
	@Column(name = "contents", columnDefinition = "TEXT")
	@Lob
	private String contents;

	@Embedded
	private UserReactionEntity userReaction;
}
