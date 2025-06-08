package com.side.rest.board.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MetadataResponseDto {

	private String createdByName;

	private Instant createdAt;

	private String modifiedByName;

	private Instant modifiedAt;
}