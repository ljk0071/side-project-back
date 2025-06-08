package com.side.rest.board.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDto {

	private String title;

	private String contents;
}