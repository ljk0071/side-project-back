package com.side.rest.domain.board.dto.response;

import com.side.domain.YesNoDeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponseDto {

    private Long id;
    private Long revision;
    private ArticleResponseDto article;
    private Long viewCount;
    private YesNoDeleteStatus status;
    private MetadataResponseDto metadata;
}
