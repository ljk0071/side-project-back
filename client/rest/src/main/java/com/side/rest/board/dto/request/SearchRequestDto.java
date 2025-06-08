package com.side.rest.board.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {

	private List<String> searchConditions;

	private String searchKeyword;
}