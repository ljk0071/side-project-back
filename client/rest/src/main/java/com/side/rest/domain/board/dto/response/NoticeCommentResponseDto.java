package com.side.rest.domain.board.dto.response;

import com.side.domain.StatusTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeCommentResponseDto {

    private Long id;
    private Long revision;
    private String contents;
    private StatusTypeEnum status;
    private Long noticeId;
    private Long parentCommentId;
    private MetadataResponseDto metadata;
}