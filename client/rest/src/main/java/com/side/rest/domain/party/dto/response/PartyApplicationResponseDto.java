package com.side.rest.domain.party.dto.response;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import com.side.rest.domain.board.dto.response.MetadataResponseDto;
import com.side.rest.domain.board.dto.response.PartyRecruitResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PartyApplicationResponseDto {

    private Long id;
    private Long revision;
    private PartyRecruitResponseDto partyRecruit;
    private ResumeResponseDto resume;
    private PartyApplicationStatusTypeEnum status;
    private MetadataResponseDto metadata;
}