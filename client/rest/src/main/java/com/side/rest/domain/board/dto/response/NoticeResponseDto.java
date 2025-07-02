package com.side.rest.domain.board.dto.response;

import com.side.domain.StatusTypeEnum;
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
    private StatusTypeEnum status;
    private MetadataResponseDto metadata;
}
