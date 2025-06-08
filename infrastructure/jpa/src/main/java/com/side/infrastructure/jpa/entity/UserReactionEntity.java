package com.side.infrastructure.jpa.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Embeddable
public class UserReactionEntity {

	@Comment("좋아요 수")
	@Column(name = "likes")
	private int likes;

	@Comment("싫어요 수")
	@Column(name = "dislikes")
	private int dislikes;
}
