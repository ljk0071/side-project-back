package com.side.rest.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PartyRecruitResponseDto {

	private Long id;

	private ArticleResponseDto article;

	private CommentResponseDto comment;

	private MetadataResponseDto metadata;
}
