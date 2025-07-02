package com.side.rest.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class MetadataResponseDto {

    private String createdByName;
    private Instant createdAt;
    private String modifiedByName;
    private Instant modifiedAt;
    private String deletedByName;
    private Instant deletedAt;
}