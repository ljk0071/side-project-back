package com.side.rest.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserReactionResponseDto {

	private long likes;
	private long dislikes;
}