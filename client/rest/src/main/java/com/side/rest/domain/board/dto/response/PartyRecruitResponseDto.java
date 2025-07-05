package com.side.rest.domain.board.dto.response;

import com.side.domain.YesNoDeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PartyRecruitResponseDto {

    private Long id;
    private Long revision;
    private Long userUniqueId;
    private ArticleResponseDto article;
    private Integer maxMembers;
    private YesNoDeleteStatus status;
    private MetadataResponseDto metadata;
}
