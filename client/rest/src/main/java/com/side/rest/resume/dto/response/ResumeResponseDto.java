package com.side.rest.resume.dto.response;

import com.side.rest.board.dto.response.MetadataResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResumeResponseDto {

	private Long id;

	private String contents;

	private MetadataResponseDto metadata;
}