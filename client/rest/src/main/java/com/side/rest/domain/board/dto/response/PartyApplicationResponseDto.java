package com.side.rest.domain.board.dto.response;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
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
    private PartyApplicationStatusTypeEnum status;
    private MetadataResponseDto metadata;
}