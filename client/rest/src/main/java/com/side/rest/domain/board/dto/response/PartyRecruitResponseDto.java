package com.side.rest.domain.board.dto.response;

import com.side.domain.YesNoDeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PartyRecruitResponseDto {

    private long id;
    private long revision;
    private long userUniqueId;
    private ArticleResponseDto article;
    private int maxMembers;
    private YesNoDeleteStatus status;
    private MetadataResponseDto metadata;
}
