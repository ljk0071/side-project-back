package com.side.rest.domain.board.dto.response;

import com.side.domain.StatusTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PartyApplicationResponseDto {

    private Long id;
    private Long revision;
    private Long partyRecruitId;
    private Long resumeId;
    private StatusTypeEnum status;
    private MetadataResponseDto metadata;
}