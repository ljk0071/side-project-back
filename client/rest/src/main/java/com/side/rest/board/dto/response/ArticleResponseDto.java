package com.side.rest.board.dto.response;

import com.side.rest.user.dto.response.UserReactionResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ArticleResponseDto {

	private String title;

	private String contents;

	private UserReactionResponseDto userReaction;
}