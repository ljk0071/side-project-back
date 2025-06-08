package com.side.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserReaction(long likes, long dislikes) {

	public static UserReaction initForInsert() {
		return new UserReaction(0L, 0L);
	}
}