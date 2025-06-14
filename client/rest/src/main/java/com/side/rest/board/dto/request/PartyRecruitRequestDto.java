package com.side.rest.board.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyRecruitRequestDto {

	private Long id;

	private ArticleRequestDto article;
}