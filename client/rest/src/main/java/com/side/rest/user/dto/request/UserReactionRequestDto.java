package com.side.rest.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReactionRequestDto {

	private long likes;
	private long dislikes;
}